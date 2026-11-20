package com.ticketing.sim;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ticketing.client.AsyncJsonClient;
import com.ticketing.common.Args;
import com.ticketing.common.Json;
import com.ticketing.common.Protocol;
import com.ticketing.common.WireResponse;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

public class SimulatorMain {

    private static final class PerfStats {
        private final Object lock = new Object();
        private final List<Long> latenciesNs = new ArrayList<>();

        private long windowStartNs;
        private long windowEndNs;

        private long completed;
        private long ok;
        private long failed;
        private long totalLatencyNs;

        public void start() {
            synchronized (lock) {
                latenciesNs.clear();
                completed = ok = failed = 0;
                totalLatencyNs = 0;
                windowStartNs = System.nanoTime();
                windowEndNs = 0;
            }
        }

        public void end() {
            synchronized (lock) {
                windowEndNs = System.nanoTime();
            }
        }

        public void record(boolean success, long latencyNs) {
            synchronized (lock) {
                completed++;
                if (success) ok++; else failed++;
                totalLatencyNs += latencyNs;
                latenciesNs.add(latencyNs);
            }
        }

        public String report(String label) {
            long start, end, c, o, f, totalNs;
            List<Long> copy;
            synchronized (lock) {
                start = windowStartNs;
                end = (windowEndNs == 0) ? System.nanoTime() : windowEndNs;
                c = completed;
                o = ok;
                f = failed;
                totalNs = totalLatencyNs;
                copy = new ArrayList<>(latenciesNs);
            }

            double durationSec = (end - start) / 1_000_000_000.0;
            double throughput = (durationSec > 0) ? (c / durationSec) : 0.0;
            double avgMs = (c > 0) ? ((totalNs / 1_000_000.0) / c) : 0.0;

            double p95Ms = 0.0;
            if (!copy.isEmpty()) {
                Collections.sort(copy);
                int idx = (int) Math.ceil(0.95 * copy.size()) - 1;
                if (idx < 0) idx = 0;
                if (idx >= copy.size()) idx = copy.size() - 1;
                p95Ms = copy.get(idx) / 1_000_000.0;
            }

            return String.format(
                    "%s | duration=%.2fs completed=%d ok=%d failed=%d | throughput=%.2f req/s | avg=%.2f ms | p95=%.2f ms",
                    label, durationSec, c, o, f, throughput, avgMs, p95Ms
            );
        }
    }

    private static final PerfStats RESERVE_STATS = new PerfStats();
    private static final PerfStats PAY_STATS = new PerfStats();
    private static final PerfStats REFUND_STATS = new PerfStats();

    public static void main(String[] args) throws Exception {
        Map<String, String> m = Args.parse(args);

        String host = Args.getStr(m, "host", "127.0.0.1");
        int port = Args.getInt(m, "port", 5050);
        int clients = Args.getInt(m, "clients", 4);
        int reserveEverySec = Args.getInt(m, "reserveEverySec", 2);
        String dataDir = Args.getStr(m, "dataDir", "data");

        System.out.println("[SIM] host=" + host + " port=" + port + " clients=" + clients + " reserveEverySec=" + reserveEverySec);

        RESERVE_STATS.start();
        PAY_STATS.start();
        REFUND_STATS.start();

        ExecutorService pool = Executors.newFixedThreadPool(clients);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 1; i <= clients; i++) {
            int id = i;
            futures.add(pool.submit(() -> runClient(host, port, "client-" + id, reserveEverySec)));
        }

        for (Future<?> f : futures) {
            try { f.get(); } catch (Exception ignored) {}
        }

        pool.shutdownNow();

        RESERVE_STATS.end();
        PAY_STATS.end();
        REFUND_STATS.end();

        System.out.println("[SIM] done.");

        StringBuilder report = new StringBuilder();
        report.append("=== PERF REPORT @ ").append(Instant.now()).append(" ===\n");
        report.append(RESERVE_STATS.report("RESERVE")).append("\n");
        report.append(PAY_STATS.report("PAY")).append("\n");
        report.append(REFUND_STATS.report("REFUND")).append("\n");

        System.out.println(report);

        Path dir = Path.of(dataDir);
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}

        String fileName = "perf_report_" + "test" + ".txt";
        Path out = dir.resolve(fileName);

        try {
            Files.writeString(out, report.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("[SIM] perf report saved to: " + out.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("[SIM] could not write perf report: " + e.getMessage());
        }
    }

    private static void runClient(String host, int port, String clientId, int reserveEverySec) {
        Random rnd = new Random();
        String cnp = randomCnp(rnd);

        try (AsyncJsonClient c = new AsyncJsonClient(host, port)) {
            c.connect();
            System.out.println("[" + clientId + "] connected (cnp=" + cnp + ")");

            while (c.isRunning()) {
                int eventId = 1 + rnd.nextInt(3);
                int categoryId = 1 + rnd.nextInt(3);
                int qty = 1 + rnd.nextInt(4);

                ObjectNode data = Json.MAPPER.createObjectNode();
                data.put("clientId", clientId);
                data.put("cnp", cnp);
                data.put("eventId", eventId);
                data.put("categoryId", categoryId);
                data.put("qty", qty);

                WireResponse reserveResp;
                long reserveStart = System.nanoTime();
                try {
                    reserveResp = c.send(Protocol.RESERVE_REQUEST, data).get(10, TimeUnit.SECONDS);
                } catch (Exception e) {
                    break;
                }
                long reserveLatency = System.nanoTime() - reserveStart;

                boolean reserveOk = reserveResp.ok;
                RESERVE_STATS.record(reserveOk, reserveLatency);

                if (reserveOk) {
                    JsonNode d = reserveResp.data;
                    String reservationId = d.get("reservationId").asText();
                    long totalPrice = d.get("totalPrice").asLong();
                    String expiresAt = d.get("expiresAt").asText();

                    System.out.println("[" + clientId + "] RESERVED ok id=" + reservationId + " qty=" + qty + " price=" + totalPrice + " expiresAt=" + expiresAt);

                    boolean willPay = rnd.nextDouble() < 0.75;
                    if (willPay) {
                        int delayMs = rnd.nextInt(9000);
                        Thread.sleep(delayMs);

                        ObjectNode pay = Json.MAPPER.createObjectNode();
                        pay.put("reservationId", reservationId);
                        pay.put("cnp", cnp);
                        pay.put("amount", totalPrice);

                        long payStart = System.nanoTime();
                        WireResponse payResp;
                        try {
                            payResp = c.send(Protocol.PAY_REQUEST, pay).get(10, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            break;
                        }
                        long payLatency = System.nanoTime() - payStart;

                        PAY_STATS.record(payResp.ok, payLatency);
                        System.out.println("[" + clientId + "] PAY ok=" + payResp.ok + " msg=" + payResp.message);

                        if (payResp.ok && rnd.nextDouble() < 0.10) {
                            ObjectNode rf = Json.MAPPER.createObjectNode();
                            rf.put("reservationId", reservationId);
                            rf.put("cnp", cnp);

                            long refundStart = System.nanoTime();
                            WireResponse rfResp;
                            try {
                                rfResp = c.send(Protocol.REFUND_REQUEST, rf).get(10, TimeUnit.SECONDS);
                            } catch (Exception e) {
                                break;
                            }
                            long refundLatency = System.nanoTime() - refundStart;

                            REFUND_STATS.record(rfResp.ok, refundLatency);
                            System.out.println("[" + clientId + "] REFUND ok=" + rfResp.ok + " msg=" + rfResp.message);
                        }
                    } else {
                        System.out.println("[" + clientId + "] NOT paying reservation " + reservationId + " (will expire)");
                    }
                } else {
                    System.out.println("[" + clientId + "] RESERVED failed msg=" + reserveResp.message);
                }

                Thread.sleep(reserveEverySec * 1000L);
            }

        } catch (Exception e) {
            System.out.println("[" + clientId + "] stopped: " + e.getMessage());
        }
    }

    private static String randomCnp(Random rnd) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 13; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
