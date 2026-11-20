package com.ticketing.server;

import com.ticketing.domain.*;
import com.ticketing.storage.SnapshotStore;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class PeriodicChecker implements Runnable {
    private final TicketingService service;
    private final SnapshotStore snapshotStore;

    public PeriodicChecker(TicketingService service, SnapshotStore snapshotStore) {
        this.service = service;
        this.snapshotStore = snapshotStore;
    }

    @Override
    public void run() {
        service.getCheckerVsRefundLock().readLock().lock();
        try {
            Instant now = Instant.now();

            List<Reservation> cleaned = service.cleanupExpiredReservations();

            List<String> integrityProblems = validateIntegrity();

            Map<Integer, Long> balancePerEvent = calcBalancePerEvent();

            StringBuilder sb = new StringBuilder();
            sb.append("=== CHECK @ ").append(now).append(" ===").append(System.lineSeparator());

            for (EventCatalog.Event e : service.getCatalog().getEvents()) {
                for (EventCatalog.Category c : e.getCategories()) {
                    sb.append("Eveniment=").append(e.eventId)
                      .append(" Categorie=").append(c.categoryId)
                      .append(" Stoc_ramas=").append(c.getAvailableStock())
                      .append(System.lineSeparator());
                }
            }

            List<Reservation> unpaid = service.getReservationsView().values().stream()
                    .filter(r -> r.status == ReservationStatus.REZERVAT)
                    .sorted(Comparator.comparing(r -> r.createdAt))
                    .collect(Collectors.toList());

            sb.append("Unpaid(Rezervat) count=").append(unpaid.size()).append(System.lineSeparator());
            for (Reservation r : unpaid) {
                sb.append("  - ").append(r.reservationId)
                  .append(" client=").append(r.clientId)
                  .append(" cnp=").append(r.cnp)
                  .append(" ev=").append(r.eventId)
                  .append(" cat=").append(r.categoryId)
                  .append(" qty=").append(r.quantity)
                  .append(" reservedAt=").append(r.createdAt)
                  .append(System.lineSeparator());
            }

            sb.append("CleanedExpired count=").append(cleaned.size()).append(System.lineSeparator());
            for (Reservation r : cleaned) {
                sb.append("  - ").append(r.reservationId)
                  .append(" ev=").append(r.eventId)
                  .append(" cat=").append(r.categoryId)
                  .append(" qty=").append(r.quantity)
                  .append(" reservedAt=").append(r.createdAt)
                  .append(System.lineSeparator());
            }

            sb.append("BalancePerEvent:").append(System.lineSeparator());
            for (Map.Entry<Integer, Long> en : balancePerEvent.entrySet()) {
                sb.append("  - Eveniment=").append(en.getKey())
                  .append(" Sold=").append(en.getValue())
                  .append(System.lineSeparator());
            }

            if (!integrityProblems.isEmpty()) {
                sb.append("INTEGRITY_PROBLEMS:").append(System.lineSeparator());
                for (String p : integrityProblems) {
                    sb.append("  - ").append(p).append(System.lineSeparator());
                }
            } else {
                sb.append("Integrity: OK").append(System.lineSeparator());
            }

            sb.append(System.lineSeparator());

            try {
                snapshotStore.appendTextBlock(sb.toString());
            } catch (IOException ignored) {}

        } finally {
            service.getCheckerVsRefundLock().readLock().unlock();
        }
    }

    private List<String> validateIntegrity() {
        Map<String, Integer> reservedCount = new HashMap<>();
        Map<String, Integer> soldCount = new HashMap<>();

        for (Reservation r : service.getReservationsView().values()) {
            String key = r.eventId + ":" + r.categoryId;
            if (r.status == ReservationStatus.REZERVAT) {
                reservedCount.merge(key, r.quantity, Integer::sum);
            } else if (r.status == ReservationStatus.VANDUT) {
                soldCount.merge(key, r.quantity, Integer::sum);
            }
        }

        List<String> problems = new ArrayList<>();
        for (EventCatalog.Event e : service.getCatalog().getEvents()) {
            for (EventCatalog.Category c : e.getCategories()) {
                String key = e.eventId + ":" + c.categoryId;
                int res = reservedCount.getOrDefault(key, 0);
                int sold = soldCount.getOrDefault(key, 0);
                int initial = c.initialStock;
                if (res + sold > initial) {
                    problems.add("ev=" + e.eventId + " cat=" + c.categoryId + " sold(" + sold + ")+reserved(" + res + ")>initial(" + initial + ")");
                }
            }
        }
        return problems;
    }

    private Map<Integer, Long> calcBalancePerEvent() {
        Map<Integer, Long> m = new TreeMap<>();

        synchronized (service.getPaymentsView()) {
            for (Payment p : service.getPaymentsView()) {
                m.merge(p.eventId, p.amount, Long::sum);
            }
        }
        synchronized (service.getRefundsView()) {
            for (Refund r : service.getRefundsView()) {
                m.merge(r.eventId, r.amount, Long::sum);
            }
        }
        return m;
    }
}
