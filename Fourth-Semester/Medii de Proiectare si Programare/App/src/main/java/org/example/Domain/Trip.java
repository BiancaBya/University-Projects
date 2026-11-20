package org.example.Domain;

import java.time.LocalDateTime;

public class Trip extends Entity<Integer>{

    private String destination;
    private LocalDateTime date;
    private int nrSeats;


    public Trip(String destination, LocalDateTime date, int nrSeats) {
        this.destination = destination;
        this.date = date;
        this.nrSeats = nrSeats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getNrSeats() {
        return nrSeats;
    }

    public void setNrSeats(int nrSeats) {
        this.nrSeats = nrSeats;
    }

    @Override
    public String toString() {
        return "destination='" + destination + '\'' +
                ", date=" + date +
                ", nrSeats=" + nrSeats;
    }
}

