package com.ticketing.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ticketing.common.Args;
import com.ticketing.common.Json;
import com.ticketing.common.Protocol;
import com.ticketing.common.WireResponse;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        Map<String, String> m = Args.parse(args);
        String host = Args.getStr(m, "host", "127.0.0.1");
        int port = Args.getInt(m, "port", 5050);

        try (AsyncJsonClient client = new AsyncJsonClient(host, port)) {
            client.connect();
            System.out.println("[CLIENT] Connected to " + host + ":" + port);
            System.out.println("Commands:");
            System.out.println("  reserve <eventId> <categoryId> <qty> <cnp>");
            System.out.println("  pay <reservationId> <cnp> <amount>");
            System.out.println("  refund <reservationId> <cnp>");
            System.out.println("  exit");

            Scanner sc = new Scanner(System.in);
            while (client.isRunning()) {
                System.out.print("> ");
                String line = sc.nextLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                if ("exit".equalsIgnoreCase(line)) break;

                String[] p = line.split("\\s+");
                try {
                    switch (p[0].toLowerCase()) {
                        case "reserve": {
                            int eventId = Integer.parseInt(p[1]);
                            int categoryId = Integer.parseInt(p[2]);
                            int qty = Integer.parseInt(p[3]);
                            String cnp = p[4];

                            ObjectNode data = Json.MAPPER.createObjectNode();
                            data.put("clientId", "interactive");
                            data.put("cnp", cnp);
                            data.put("eventId", eventId);
                            data.put("categoryId", categoryId);
                            data.put("qty", qty);

                            WireResponse resp = client.send(Protocol.RESERVE_REQUEST, data).get(10, TimeUnit.SECONDS);
                            System.out.println(Json.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(resp));
                            break;
                        }
                        case "pay": {
                            String reservationId = p[1];
                            String cnp = p[2];
                            long amount = Long.parseLong(p[3]);

                            ObjectNode data = Json.MAPPER.createObjectNode();
                            data.put("reservationId", reservationId);
                            data.put("cnp", cnp);
                            data.put("amount", amount);

                            WireResponse resp = client.send(Protocol.PAY_REQUEST, data).get(10, TimeUnit.SECONDS);
                            System.out.println(Json.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(resp));
                            break;
                        }
                        case "refund": {
                            String reservationId = p[1];
                            String cnp = p[2];

                            ObjectNode data = Json.MAPPER.createObjectNode();
                            data.put("reservationId", reservationId);
                            data.put("cnp", cnp);

                            WireResponse resp = client.send(Protocol.REFUND_REQUEST, data).get(10, TimeUnit.SECONDS);
                            System.out.println(Json.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(resp));
                            break;
                        }
                        default:
                            System.out.println("Unknown command.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        System.out.println("[CLIENT] Exiting.");
    }
}
