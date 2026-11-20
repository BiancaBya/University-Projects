package org.example.Domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table( name = "Trips" )
public class Trip extends org.example.Domain.Entity<Integer>{

    private String destination;
    private LocalDateTime date;
    private int nrSeats;

    public Trip() {}

    public Trip(String destination, LocalDateTime date, int nrSeats) {
        this.destination = destination;
        this.date = date;
        this.nrSeats = nrSeats;
    }

    @Id
    @GeneratedValue(strategy=IDENTITY)
    public Integer getId() {
        return super.getId();
    }
    public void setId(int id) {
        super.setId(id);
    }

    @Column(name = "destination")
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Column(name = "date")
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Column(name = "nrSeats")
    public int getNrSeats() {
        return nrSeats;
    }

    public void setNrSeats(int nrSeats) {
        this.nrSeats = nrSeats;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + getId() + '\'' +
                "destination='" + destination + '\'' +
                ", date=" + date + '\'' +
                ", nrSeats=" + nrSeats + '\'' +
                "}";
    }
}

