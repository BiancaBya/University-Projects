package com.ticketing.server;

import com.ticketing.domain.*;
import com.ticketing.storage.JsonlStore;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.*;

public class TicketingService {

    public static class ReserveResult {
        public final boolean ok;
        public final String message;
        public final Reservation reservation;
        public final Instant expiresAt;

        public ReserveResult(boolean ok, String message, Reservation reservation, Instant expiresAt) {
            this.ok = ok;
            this.message = message;
            this.reservation = reservation;
            this.expiresAt = expiresAt;
        }
    }

    public static class SimpleResult {
        public final boolean ok;
        public final String message;

        public SimpleResult(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }
    }

    private final EventCatalog catalog;
    private final Duration tPlata;

    private final Map<String, Reservation> reservations = new HashMap<>();
    private final Object reservationsMutex = new Object();

    private final List<Payment> payments = new ArrayList<>();
    private final Object paymentsMutex = new Object();

    private final List<Refund> refunds = new ArrayList<>();
    private final Object refundsMutex = new Object();

    private final Map<String, ReentrantLock> categoryLocks = new HashMap<>();
    private final Object categoryLocksMutex = new Object();

    private final ReadWriteLock checkerVsRefundLock = new ReentrantReadWriteLock(true);

    private final JsonlStore planificareStore;
    private final JsonlStore platiStore;
    private final JsonlStore rambursariStore;

    public TicketingService(EventCatalog catalog,
                            Duration tPlata,
                            JsonlStore planificareStore,
                            JsonlStore platiStore,
                            JsonlStore rambursariStore) {
        this.catalog = catalog;
        this.tPlata = tPlata;
        this.planificareStore = planificareStore;
        this.platiStore = platiStore;
        this.rambursariStore = rambursariStore;
    }

    public ReadWriteLock getCheckerVsRefundLock() {
        return checkerVsRefundLock;
    }

    private ReentrantLock lockFor(int eventId, int categoryId) {
        String key = eventId + ":" + categoryId;
        synchronized (categoryLocksMutex) {
            ReentrantLock existing = categoryLocks.get(key);
            if (existing != null) return existing;
            ReentrantLock created = new ReentrantLock(true);
            categoryLocks.put(key, created);
            return created;
        }
    }

    public EventCatalog getCatalog() {
        return catalog;
    }

    public Map<String, Reservation> getReservationsView() {
        synchronized (reservationsMutex) {
            return new HashMap<>(reservations);
        }
    }

    public List<Payment> getPaymentsView() {
        synchronized (paymentsMutex) {
            return new ArrayList<>(payments);
        }
    }

    public List<Refund> getRefundsView() {
        synchronized (refundsMutex) {
            return new ArrayList<>(refunds);
        }
    }

    public Duration getTPlata() {
        return tPlata;
    }

    private Reservation reservationsGet(String reservationId) {
        synchronized (reservationsMutex) {
            return reservations.get(reservationId);
        }
    }

    private void reservationsPut(String reservationId, Reservation r) {
        synchronized (reservationsMutex) {
            reservations.put(reservationId, r);
        }
    }

    private Reservation reservationsRemove(String reservationId) {
        synchronized (reservationsMutex) {
            return reservations.remove(reservationId);
        }
    }

    private Collection<Reservation> reservationsValuesSnapshot() {
        synchronized (reservationsMutex) {
            return new ArrayList<>(reservations.values());
        }
    }

    public ReserveResult reserve(String clientId, String cnp, int eventId, int categoryId, int qty) {
        checkerVsRefundLock.readLock().lock();
        try {
            if (qty <= 0) return new ReserveResult(false, "qty must be > 0", null, null);

            EventCatalog.Event e = catalog.getEvent(eventId);
            if (e == null) return new ReserveResult(false, "Unknown eventId", null, null);

            EventCatalog.Category c = e.getCategory(categoryId);
            if (c == null) return new ReserveResult(false, "Unknown categoryId", null, null);

            ReentrantLock catLock = lockFor(eventId, categoryId);
            catLock.lock();
            try {
                if (c.getAvailableStock() < qty) {
                    return new ReserveResult(false, "Rezervare esuata (stoc insuficient)", null, null);
                }

                c.decreaseAvailable(qty);
                String reservationId = UUID.randomUUID().toString();
                Instant now = Instant.now();
                long totalPrice = qty * c.price;

                Reservation r = new Reservation(
                        reservationId, clientId, cnp, eventId, categoryId, qty,
                        ReservationStatus.REZERVAT, totalPrice, now
                );

                reservationsPut(reservationId, r);

                try {
                    planificareStore.appendObject(Map.of(
                            "id_rezervare", r.reservationId,
                            "id_client", r.clientId,
                            "cnp", r.cnp,
                            "eveniment", r.eventId,
                            "categorie", r.categoryId,
                            "numar_bilete", r.quantity,
                            "status", r.status.toString(),
                            "pret_total", r.totalPrice,
                            "ora_rezervare", r.createdAt.toString()
                    ));
                } catch (IOException io) {
                    return new ReserveResult(true, "Rezervare reusita (dar persistenta a esuat)", r, now.plus(tPlata));
                }

                return new ReserveResult(true, "Rezervare reusita", r, now.plus(tPlata));
            } finally {
                catLock.unlock();
            }
        } finally {
            checkerVsRefundLock.readLock().unlock();
        }
    }

    public SimpleResult pay(String reservationId, String cnp, long amount) {
        checkerVsRefundLock.readLock().lock();
        try {
            Reservation r = reservationsGet(reservationId);
            if (r == null) return new SimpleResult(false, "Rezervare inexistenta");
            if (!Objects.equals(r.cnp, cnp)) return new SimpleResult(false, "CNP nu corespunde");

            if (r.status == ReservationStatus.VANDUT) return new SimpleResult(false, "Deja vandut");
            if (r.status != ReservationStatus.REZERVAT) return new SimpleResult(false, "Status invalid: " + r.status);

            Instant now = Instant.now();
            boolean expired = now.isAfter(r.createdAt.plus(tPlata));

            EventCatalog.Event e = catalog.getEvent(r.eventId);
            EventCatalog.Category c = e.getCategory(r.categoryId);
            ReentrantLock catLock = lockFor(r.eventId, r.categoryId);

            catLock.lock();
            try {
                Reservation current = reservationsGet(reservationId);
                if (current == null) return new SimpleResult(false, "Rezervare inexistenta");
                if (current.status != ReservationStatus.REZERVAT) return new SimpleResult(false, "Status invalid: " + current.status);

                if (expired) {
                    current.status = ReservationStatus.ANULAT_EXPIRAT;
                    reservationsRemove(reservationId);
                    c.increaseAvailable(current.quantity);

                    try {
                        planificareStore.appendObject(Map.of(
                                "id_rezervare", current.reservationId,
                                "id_client", current.clientId,
                                "cnp", current.cnp,
                                "eveniment", current.eventId,
                                "categorie", current.categoryId,
                                "numar_bilete", current.quantity,
                                "status", "ANULAT_EXPIRAT",
                                "pret_total", current.totalPrice,
                                "ora_rezervare", current.createdAt.toString(),
                                "ora_anulare", now.toString()
                        ));
                    } catch (IOException ignored) {}

                    return new SimpleResult(false, "Plata esuata: rezervarea a expirat");
                }

                if (amount != current.totalPrice) {
                    return new SimpleResult(false, "Suma incorecta. Asteptat: " + current.totalPrice);
                }

                current.status = ReservationStatus.VANDUT;

                Payment p = new Payment(UUID.randomUUID().toString(), current.reservationId, now, current.cnp, amount, current.eventId);
                synchronized (paymentsMutex) {
                    payments.add(p);
                }

                try {
                    platiStore.appendObject(Map.of(
                            "id_plata", p.paymentId,
                            "id_rezervare", p.reservationId,
                            "data", p.at.toString(),
                            "cnp", p.cnp,
                            "suma", p.amount
                    ));
                    planificareStore.appendObject(Map.of(
                            "id_rezervare", current.reservationId,
                            "status", "VANDUT",
                            "ora_vanzare", now.toString()
                    ));
                } catch (IOException ignored) {}

                return new SimpleResult(true, "Plata inregistrata. Bilete vandute.");
            } finally {
                catLock.unlock();
            }
        } finally {
            checkerVsRefundLock.readLock().unlock();
        }
    }

    public SimpleResult refund(String reservationId, String cnp) {
        checkerVsRefundLock.writeLock().lock();
        try {
            Reservation r = reservationsGet(reservationId);
            if (r == null) return new SimpleResult(false, "Rezervare inexistenta");
            if (!Objects.equals(r.cnp, cnp)) return new SimpleResult(false, "CNP nu corespunde");
            if (r.status != ReservationStatus.VANDUT) return new SimpleResult(false, "Se poate rambursa doar o rezervare VANDUT");

            Instant now = Instant.now();
            EventCatalog.Event e = catalog.getEvent(r.eventId);
            EventCatalog.Category c = e.getCategory(r.categoryId);
            ReentrantLock catLock = lockFor(r.eventId, r.categoryId);

            catLock.lock();
            try {
                Reservation current = reservationsGet(reservationId);
                if (current == null) return new SimpleResult(false, "Rezervare inexistenta");
                if (current.status != ReservationStatus.VANDUT) return new SimpleResult(false, "Status invalid: " + current.status);

                current.status = ReservationStatus.RAMBURSAT;
                c.increaseAvailable(current.quantity);

                long refundAmount = -current.totalPrice;
                Refund rf = new Refund(UUID.randomUUID().toString(), current.reservationId, now, current.cnp, refundAmount, current.eventId);

                synchronized (refundsMutex) {
                    refunds.add(rf);
                }

                try {
                    rambursariStore.appendObject(Map.of(
                            "id_rambursare", rf.refundId,
                            "id_rezervare", rf.reservationId,
                            "data", rf.at.toString(),
                            "cnp", rf.cnp,
                            "suma", rf.amount
                    ));
                    planificareStore.appendObject(Map.of(
                            "id_rezervare", current.reservationId,
                            "status", "RAMBURSAT",
                            "ora_rambursare", now.toString()
                    ));
                } catch (IOException ignored) {}

                return new SimpleResult(true, "Rambursare efectuata.");
            } finally {
                catLock.unlock();
            }
        } finally {
            checkerVsRefundLock.writeLock().unlock();
        }
    }

    public List<Reservation> cleanupExpiredReservations() {
        List<Reservation> cleaned = new ArrayList<>();
        Instant now = Instant.now();

        for (Reservation r : reservationsValuesSnapshot()) {
            if (r.status != ReservationStatus.REZERVAT) continue;
            if (!now.isAfter(r.createdAt.plus(tPlata))) continue;

            ReentrantLock catLock = lockFor(r.eventId, r.categoryId);
            catLock.lock();
            try {
                Reservation current = reservationsGet(r.reservationId);
                if (current == null) continue;
                if (current.status != ReservationStatus.REZERVAT) continue;
                if (!now.isAfter(current.createdAt.plus(tPlata))) continue;

                current.status = ReservationStatus.ANULAT_EXPIRAT;
                reservationsRemove(current.reservationId);

                EventCatalog.Event e = catalog.getEvent(current.eventId);
                EventCatalog.Category c = e.getCategory(current.categoryId);
                c.increaseAvailable(current.quantity);

                cleaned.add(current);

                try {
                    planificareStore.appendObject(Map.of(
                            "id_rezervare", current.reservationId,
                            "status", "ANULAT_EXPIRAT",
                            "ora_anulare", now.toString()
                    ));
                } catch (IOException ignored) {}

            } finally {
                catLock.unlock();
            }
        }
        return cleaned;
    }

    public Map<String, Integer> getAvailableStocksSnapshot() {
        Map<String, Integer> out = new LinkedHashMap<>();
        for (EventCatalog.Event e : catalog.getEvents()) {
            for (EventCatalog.Category c : e.getCategories()) {
                out.put(e.eventId + ":" + c.categoryId, c.getAvailableStock());
            }
        }
        return out;
    }
}
