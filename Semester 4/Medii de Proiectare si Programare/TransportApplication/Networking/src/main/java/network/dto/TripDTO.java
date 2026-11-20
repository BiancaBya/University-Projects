package network.dto;

import org.example.Domain.Entity;

import java.time.LocalDateTime;

public class TripDTO extends Entity<Integer> {

    private String destination;
    private LocalDateTime date;
    private int nrSeats;
    private int id_trip;

    public TripDTO(String destination, LocalDateTime date, int nrSeats) {
        this.destination = destination;
        this.date = date;
        this.nrSeats = nrSeats;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getNrSeats() {
        return nrSeats;
    }

    public int getId_trip() {
        return id_trip;
    }

    public void setId_trip(int id_trip) {
        this.id_trip = id_trip;
    }

    @Override
    public String toString() {
        return "TripDTO{" +
                "id_trip =" + id_trip + '\'' +
                "destination='" + destination + '\'' +
                ", date=" + date +
                ", nrSeats=" + nrSeats +
                '}';
    }
}
