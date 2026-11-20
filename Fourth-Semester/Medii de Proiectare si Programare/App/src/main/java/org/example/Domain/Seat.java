package org.example.Domain;

public class Seat {
    private final Integer seatNumber;
    private final String clientName;

    public Seat(Integer seatNumber, String clientName) {
        this.seatNumber = seatNumber;
        this.clientName = clientName;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public String getClientName() {
        return clientName;
    }
}
