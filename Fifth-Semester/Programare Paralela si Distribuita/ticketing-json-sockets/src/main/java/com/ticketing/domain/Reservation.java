package com.ticketing.domain;

import java.time.Instant;

public class Reservation {
    public final String reservationId;
    public final String clientId;
    public final String cnp;
    public final int eventId;
    public final int categoryId;
    public final int quantity;
    public ReservationStatus status;
    public final long totalPrice;
    public final Instant createdAt;

    public Reservation(String reservationId,
                       String clientId,
                       String cnp,
                       int eventId,
                       int categoryId,
                       int quantity,
                       ReservationStatus status,
                       long totalPrice,
                       Instant createdAt) {
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.cnp = cnp;
        this.eventId = eventId;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }
}
