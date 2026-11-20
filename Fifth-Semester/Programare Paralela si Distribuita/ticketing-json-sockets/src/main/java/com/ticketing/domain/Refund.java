package com.ticketing.domain;

import java.time.Instant;

public class Refund {
    public final String refundId;
    public final String reservationId;
    public final Instant at;
    public final String cnp;
    public final long amount;
    public final int eventId;

    public Refund(String refundId, String reservationId, Instant at, String cnp, long amount, int eventId) {
        this.refundId = refundId;
        this.reservationId = reservationId;
        this.at = at;
        this.cnp = cnp;
        this.amount = amount;
        this.eventId = eventId;
    }
}
