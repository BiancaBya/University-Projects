package com.ticketing.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ticketing.common.Json;
import com.ticketing.common.WireMessage;
import com.ticketing.common.WireResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class AsyncJsonClient implements Closeable {
    private final String host;
    private final int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private final Map<String, CompletableFuture<WireResponse>> pending = new HashMap<>();
    private final Object pendingLock = new Object();

    private final ExecutorService readerThread = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    public AsyncJsonClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        this.socket = new Socket(host, port);
        this.socket.setTcpNoDelay(true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        this.running = true;

        readerThread.submit(() -> {
            try {
                String line;
                while (running && (line = in.readLine()) != null) {
                    WireResponse resp = Json.MAPPER.readValue(line, WireResponse.class);

                    if ("SERVER_SHUTDOWN".equals(resp.type)) {
                        synchronized (pendingLock) {
                            for (CompletableFuture<WireResponse> f : pending.values()) {
                                f.completeExceptionally(
                                        new RuntimeException("Server shutdown")
                                );
                            }
                            pending.clear();
                        }
                        running = false;
                        break;
                    }

                    if (resp.requestId != null) {
                        CompletableFuture<WireResponse> f;
                        synchronized (pendingLock) {
                            f = pending.remove(resp.requestId);
                        }
                        if (f != null) {
                            f.complete(resp);
                        }
                    }
                }
            } catch (Exception e) {
                synchronized (pendingLock) {
                    for (CompletableFuture<WireResponse> f : pending.values()) {
                        f.completeExceptionally(e);
                    }
                    pending.clear();
                }
            } finally {
                running = false;
            }
        });
    }

    public CompletableFuture<WireResponse> send(String type, ObjectNode data) {
        String requestId = UUID.randomUUID().toString();
        WireMessage msg = new WireMessage(type, requestId, data);
        CompletableFuture<WireResponse> f = new CompletableFuture<>();

        synchronized (pendingLock) {
            pending.put(requestId, f);
        }

        try {
            String line = Json.MAPPER.writeValueAsString(msg);
            out.println(line);
            out.flush();
        } catch (Exception e) {
            synchronized (pendingLock) {
                pending.remove(requestId);
            }
            f.completeExceptionally(e);
        }

        return f;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void close() throws IOException {
        running = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
        readerThread.shutdownNow();
    }
}
