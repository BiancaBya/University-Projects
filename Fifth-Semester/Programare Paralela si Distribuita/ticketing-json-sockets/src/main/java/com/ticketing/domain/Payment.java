package com.ticketing.domain;

import java.time.Instant;

public class Payment {
    public final String paymentId;
    public final String reservationId;
    public final Instant at;
    public final String cnp;
    public final long amount;
    public final int eventId;

    public Payment(String paymentId, String reservationId, Instant at, String cnp, long amount, int eventId) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.at = at;
        this.cnp = cnp;
        this.amount = amount;
        this.eventId = eventId;
    }
}
