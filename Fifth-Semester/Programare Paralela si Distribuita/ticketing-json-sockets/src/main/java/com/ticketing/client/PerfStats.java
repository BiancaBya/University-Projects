package com.ticketing.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerfStats {
    private final Object lock = new Object();

    private long t0Ns = -1;
    private long t1Ns = -1;

    private long completed = 0;
    private long ok = 0;
    private long failed = 0;

    private long totalLatencyNs = 0;
    private final List<Long> latenciesNs = new ArrayList<>();

    public void startWindow() {
        synchronized (lock) {
            t0Ns = System.nanoTime();
            t1Ns = -1;
            completed = ok = failed = 0;
            totalLatencyNs = 0;
            latenciesNs.clear();
        }
    }

    public void endWindow() {
        synchronized (lock) {
            t1Ns = System.nanoTime();
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
        long localT0, localT1, localCompleted, localOk, localFailed, localTotalLatency;
        List<Long> localLat;

        synchronized (lock) {
            localT0 = t0Ns;
            localT1 = (t1Ns > 0) ? t1Ns : System.nanoTime();
            localCompleted = completed;
            localOk = ok;
            localFailed = failed;
            localTotalLatency = totalLatencyNs;
            localLat = new ArrayList<>(latenciesNs);
        }

        double seconds = (localT1 - localT0) / 1_000_000_000.0;
        double throughput = (seconds > 0) ? (localCompleted / seconds) : 0.0;

        double avgMs = (localCompleted > 0)
                ? (localTotalLatency / 1_000_000.0) / localCompleted
                : 0.0;

        double p95Ms = 0.0;
        if (!localLat.isEmpty()) {
            Collections.sort(localLat);
            int idx = (int) Math.ceil(0.95 * localLat.size()) - 1;
            if (idx < 0) idx = 0;
            p95Ms = localLat.get(idx) / 1_000_000.0;
        }

        return String.format(
                "[PERF] %s | duration=%.2fs completed=%d ok=%d failed=%d | throughput=%.2f req/s | avg=%.2f ms | p95=%.2f ms",
                label, seconds, localCompleted, localOk, localFailed, throughput, avgMs, p95Ms
        );
    }
}


