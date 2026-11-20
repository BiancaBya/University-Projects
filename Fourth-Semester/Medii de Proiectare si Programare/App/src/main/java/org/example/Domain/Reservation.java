package org.example.Domain;

public class Reservation extends Entity<Integer>{

    private Client client;
    private Trip trip;
    private int nrReservedSeats;

    public Reservation(Client client, Trip trip, int nrReservedSeats) {
        this.client = client;
        this.trip = trip;
        this.nrReservedSeats = nrReservedSeats;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getNrReservedSeats() {
        return nrReservedSeats;
    }

    public void setNrReservedSeats(int nrReservedSeats) {
        this.nrReservedSeats = nrReservedSeats;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "client=" + client +
                ", trip=" + trip +
                ", nrReservedSeats=" + nrReservedSeats +
                '}';
    }
}
