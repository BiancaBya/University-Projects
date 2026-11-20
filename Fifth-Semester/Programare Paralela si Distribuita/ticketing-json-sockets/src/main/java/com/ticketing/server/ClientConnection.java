package com.ticketing.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ticketing.common.Json;
import com.ticketing.common.Protocol;
import com.ticketing.common.WireMessage;
import com.ticketing.common.WireResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class ClientConnection implements Runnable {
    private final Socket socket;
    private final TicketingService service;
    private final ExecutorService requestPool;
    private final ServerRuntime runtime;

    private volatile boolean running = true;
    private PrintWriter out;

    public ClientConnection(Socket socket, TicketingService service, ExecutorService requestPool, ServerRuntime runtime) {
        this.socket = socket;
        this.service = service;
        this.requestPool = requestPool;
        this.runtime = runtime;
    }

    public void sendShutdown(String message) {
        try {
            ObjectNode data = Json.MAPPER.createObjectNode();
            data.put("message", message);
            WireResponse resp = new WireResponse(Protocol.SERVER_SHUTDOWN, "server", true, message, data);
            send(resp);
        } catch (Exception ignored) {}
        close();
    }

    private void send(WireResponse resp) {
        if (out == null) return;
        try {
            String line = Json.MAPPER.writeValueAsString(resp);
            out.println(line);
            out.flush();
        } catch (Exception ignored) {}
    }

    public void close() {
        running = false;
        try { socket.close(); } catch (IOException ignored) {}
    }

    @Override
    public void run() {
        runtime.register(this);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            this.out = writer;

            String line;
            while (running && (line = in.readLine()) != null) {
                final String reqLine = line;

                Future<WireResponse> future = requestPool.submit(() -> handle(reqLine));

                WireResponse resp;
                try {
                    resp = future.get(30, TimeUnit.SECONDS);
                } catch (TimeoutException te) {
                    resp = new WireResponse(Protocol.ERROR, "unknown", false, "Timeout processing request", Json.MAPPER.createObjectNode());
                } catch (Exception e) {
                    resp = new WireResponse(Protocol.ERROR, "unknown", false, "Error: " + e.getMessage(), Json.MAPPER.createObjectNode());
                }

                send(resp);
            }
        } catch (IOException ignored) {
        } finally {
            runtime.unregister(this);
            close();
        }
    }

    private WireResponse handle(String line) {
        try {
            WireMessage msg = Json.MAPPER.readValue(line, WireMessage.class);
            if (msg.type == null) {
                return new WireResponse(Protocol.ERROR, msg.requestId, false, "Missing type", Json.MAPPER.createObjectNode());
            }

            switch (msg.type) {
                case Protocol.PING:
                    return new WireResponse(Protocol.PING_RESPONSE, msg.requestId, true, "pong", Json.MAPPER.createObjectNode());

                case Protocol.RESERVE_REQUEST: {
                    JsonNode d = msg.data;
                    String clientId = d.get("clientId").asText();
                    String cnp = d.get("cnp").asText();
                    int eventId = d.get("eventId").asInt();
                    int categoryId = d.get("categoryId").asInt();
                    int qty = d.get("qty").asInt();

                    TicketingService.ReserveResult rr = service.reserve(clientId, cnp, eventId, categoryId, qty);

                    ObjectNode data = Json.MAPPER.createObjectNode();
                    data.put("reservationOk", rr.ok);
                    data.put("message", rr.message);
                    if (rr.ok && rr.reservation != null) {
                        data.put("reservationId", rr.reservation.reservationId);
                        data.put("totalPrice", rr.reservation.totalPrice);
                        data.put("reservedAt", rr.reservation.createdAt.toString());
                        data.put("expiresAt", rr.expiresAt.toString());
                        data.put("eventId", rr.reservation.eventId);
                        data.put("categoryId", rr.reservation.categoryId);
                        data.put("qty", rr.reservation.quantity);
                    }
                    return new WireResponse(Protocol.RESERVE_RESPONSE, msg.requestId, rr.ok, rr.message, data);
                }

                case Protocol.PAY_REQUEST: {
                    JsonNode d = msg.data;
                    String reservationId = d.get("reservationId").asText();
                    String cnp = d.get("cnp").asText();
                    long amount = d.get("amount").asLong();

                    TicketingService.SimpleResult pr = service.pay(reservationId, cnp, amount);

                    ObjectNode data = Json.MAPPER.createObjectNode();
                    data.put("paid", pr.ok);
                    data.put("message", pr.message);
                    return new WireResponse(Protocol.PAY_RESPONSE, msg.requestId, pr.ok, pr.message, data);
                }

                case Protocol.REFUND_REQUEST: {
                    JsonNode d = msg.data;
                    String reservationId = d.get("reservationId").asText();
                    String cnp = d.get("cnp").asText();

                    TicketingService.SimpleResult rr = service.refund(reservationId, cnp);

                    ObjectNode data = Json.MAPPER.createObjectNode();
                    data.put("refunded", rr.ok);
                    data.put("message", rr.message);
                    return new WireResponse(Protocol.REFUND_RESPONSE, msg.requestId, rr.ok, rr.message, data);
                }

                default:
                    return new WireResponse(Protocol.ERROR, msg.requestId, false, "Unknown type: " + msg.type, Json.MAPPER.createObjectNode());
            }
        } catch (Exception e) {
            ObjectNode data = Json.MAPPER.createObjectNode();
            data.put("error", e.getClass().getSimpleName());
            data.put("details", e.getMessage() == null ? "" : e.getMessage());
            return new WireResponse(Protocol.ERROR, "unknown", false, "Bad request", data);
        }
    }
}
