package com.ticketing.server;

import com.ticketing.common.Args;
import com.ticketing.domain.EventCatalog;
import com.ticketing.storage.JsonlStore;
import com.ticketing.storage.SnapshotStore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        Map<String, String> m = Args.parse(args);

        int port = Args.getInt(m, "port", 5050);
        int threads = Args.getInt(m, "threads", 8);
        int tPlataSec = Args.getInt(m, "tPlataSec", 5);
        int checkIntervalSec = Args.getInt(m, "checkIntervalSec", 5);
        long shutdownAfterSec = Args.getLong(m, "shutdownAfterSec", 180);
        String dataDir = Args.getStr(m, "dataDir", "data");

        Path dataPath = Path.of(dataDir);
        JsonlStore planificare = new JsonlStore(dataPath.resolve("planificare.jsonl"));
        JsonlStore plati = new JsonlStore(dataPath.resolve("plati.jsonl"));
        JsonlStore rambursari = new JsonlStore(dataPath.resolve("rambursari.jsonl"));
        SnapshotStore snapshots = new SnapshotStore(dataPath.resolve("checker_snapshots_" + checkIntervalSec + "s.txt"));

        EventCatalog catalog = EventCatalog.sampleCatalog();
        TicketingService service = new TicketingService(catalog, Duration.ofSeconds(tPlataSec), planificare, plati, rambursari);

        ExecutorService requestPool = Executors.newFixedThreadPool(threads);
        ExecutorService connectionPool = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        ServerRuntime runtime = new ServerRuntime();

        scheduler.scheduleAtFixedRate(
                new PeriodicChecker(service, snapshots),
                checkIntervalSec, checkIntervalSec, TimeUnit.SECONDS
        );

        scheduler.schedule(() -> {
            System.out.println("[SERVER] Shutdown triggered. Active clients: " + runtime.activeConnections());
            runtime.broadcastShutdown("Server shutting down now.");
            try { requestPool.shutdownNow(); } catch (Exception ignored) {}
            try { connectionPool.shutdownNow(); } catch (Exception ignored) {}
        }, shutdownAfterSec, TimeUnit.SECONDS);

        System.out.println("[SERVER] Starting on port " + port);
        System.out.println("[SERVER] threads(P)=" + threads + " T_plata=" + tPlataSec + "s checkerInterval=" + checkIntervalSec + "s shutdownAfter=" + shutdownAfterSec + "s");
        System.out.println("[SERVER] dataDir=" + dataDir);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);

            scheduler.schedule(() -> {
                System.out.println("[SERVER] Shutdown triggered. Active clients: " + runtime.activeConnections());
                runtime.broadcastShutdown("Server shutting down now.");

                try { serverSocket.close(); } catch (Exception ignored) {}

                try { requestPool.shutdownNow(); } catch (Exception ignored) {}
                try { connectionPool.shutdownNow(); } catch (Exception ignored) {}
                try { scheduler.shutdownNow(); } catch (Exception ignored) {}
            }, shutdownAfterSec, TimeUnit.SECONDS);

            long endAt = System.currentTimeMillis() + shutdownAfterSec * 1000L;

            while (System.currentTimeMillis() < endAt && !requestPool.isShutdown()) {
                try {
                    Socket s = serverSocket.accept();
                    s.setTcpNoDelay(true);
                    ClientConnection conn = new ClientConnection(s, service, requestPool, runtime);
                    connectionPool.submit(conn);
                } catch (IOException e) {
                    if (requestPool.isShutdown()) break;
                    break;
                }
            }
        } finally {
            runtime.broadcastShutdown("Server shutting down now.");
            scheduler.shutdownNow();
            requestPool.shutdownNow();
            connectionPool.shutdownNow();
            System.out.println("[SERVER] Stopped.");
            System.exit(0);
        }
    }
}
