package network.dto;

import org.example.Domain.Client;
import org.example.Domain.Entity;
import org.example.Domain.Trip;

public class ReservationDTO extends Entity<Integer> {

    private Client client;
    private Trip trip;
    private int nrReservedSeats;

    public ReservationDTO(Client client, Trip trip, int nrReservedSeats) {
        this.client = client;
        this.trip = trip;
        this.nrReservedSeats = nrReservedSeats;
    }

    public Client getClient() {
        return client;
    }

    public Trip getTrip() {
        return trip;
    }

    public int getNrReservedSeats() {
        return nrReservedSeats;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "client=" + client +
                ", trip=" + trip +
                ", nrReservedSeats=" + nrReservedSeats +
                '}';
    }
}
