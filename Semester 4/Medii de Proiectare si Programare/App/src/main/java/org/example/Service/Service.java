package org.example.Service;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;
import org.example.Repository.AdminRepoInterface;
import org.example.Repository.ClientRepoInterface;
import org.example.Repository.ReservationRepoInterface;
import org.example.Repository.TripRepoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Service {

    private final AdminRepoInterface adminRepo;;
    private final ClientRepoInterface clientRepo;
    private final TripRepoInterface tripRepo;
    private final ReservationRepoInterface reservationRepo;

    @Autowired
    public Service(AdminRepoInterface adminRepo, ClientRepoInterface clientRepo, TripRepoInterface tripRepo, ReservationRepoInterface reservationRepo) {
        this.adminRepo = adminRepo;
        this.clientRepo = clientRepo;
        this.tripRepo = tripRepo;
        this.reservationRepo = reservationRepo;
    }

    public Optional<Admin> loginAdmin(String email, String password) {
        for (Admin admin : adminRepo.findAll()) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return Optional.of(admin);
            }
        }
        return Optional.empty();
    }

    public List<Trip> findAllTrips(){
        List<Trip> trips = new ArrayList<>();
        tripRepo.findAll().forEach(trips::add);
        return trips;
    }

    public List<Reservation> getReservationsForTrip(Trip trip) {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : reservationRepo.findAll()) {
            if (reservation.getTrip().equals(trip)) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public Trip findTripByTimeAndDestination(LocalDateTime time, String destination) {
        for (Trip trip : tripRepo.findAll()) {
            if(trip.getDate().equals(time) && trip.getDestination().equals(destination)) {
                return trip;
            }
        }
        return null;
    }

    public Client findClientByNames(String firstName, String lastName) {
        for (Client client : clientRepo.findAll()) {
            if (client.getFirstName().equals(firstName) && client.getLastName().equals(lastName)) {
                return client;
            }
        }
        return null;
    }

    public Optional<Client> saveClient(Client client) {
        return clientRepo.save(client);
    }

    public Optional<Reservation> saveReservation(Reservation reservation) {
        return reservationRepo.save(reservation);
    }

    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepo.findAll().forEach(reservations::add);
        return reservations;
    }

    public Optional<Trip> updateTrip(Trip trip) {
        return tripRepo.update(trip);
    }
}



