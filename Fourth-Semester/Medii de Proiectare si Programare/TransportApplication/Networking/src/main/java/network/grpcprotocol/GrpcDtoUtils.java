package network.grpcprotocol;

import network.grpcprotocol.TransportProto.*;
import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Trip;
import org.example.Domain.Reservation;
import org.example.Domain.Seat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class GrpcDtoUtils {

    // --- Admin -------------------------------------------------------------

    public static Admin getFromDTO(TransportProto.Admin adminDTO) {
        if (adminDTO == null) return null;
        String email = adminDTO.getEmail();
        String password = adminDTO.getPassword();
        int id = adminDTO.getId();
        Admin admin = new Admin(email, password);
        admin.setId(id);
        return admin;
    }

    public static TransportProto.Admin getDTO(Admin admin) {
        if (admin == null) return null;
        return TransportProto.Admin.newBuilder()
                .setEmail(admin.getEmail())
                .setPassword(admin.getPassword())
                .setId(admin.getId())
                .build();
    }

    // --- Client -------------------------------------------------------------

    public static Client getFromDTO(TransportProto.Client clientDTO) {
        if (clientDTO == null) return null;
        String firstName = clientDTO.getFirstName();
        String lastName = clientDTO.getLastName();
        String email = clientDTO.getEmail();
        String phone = clientDTO.getPhone();
        int id = clientDTO.getId();
        Client client = new Client(firstName, lastName, email, phone);
        client.setId(id);
        return client;
    }

    public static TransportProto.Client getDTO(Client client) {
        if (client == null) return null;
        return TransportProto.Client.newBuilder()
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setEmail(client.getEmail())
                .setPhone(client.getPhone())
                .setId(client.getId())
                .build();
    }

    // --- Trip ---------------------------------------------------------------

    public static Trip getFromDTO(TransportProto.Trip tripDTO) {
        if (tripDTO == null) return null;
        String destination = tripDTO.getDestination();
        LocalDateTime date = LocalDateTime.parse(tripDTO.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        int nrSeats = tripDTO.getNrSeats();
        int id = tripDTO.getId();
        Trip trip = new Trip(destination, date, nrSeats);
        trip.setId(id);
        return trip;
    }

    public static TransportProto.Trip getDTO(Trip trip) {
        if (trip == null) return null;
        return TransportProto.Trip.newBuilder()
                .setDestination(trip.getDestination())
                .setDate(trip.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setNrSeats(trip.getNrSeats())
                .setId(trip.getId())
                .build();
    }

    // --- Reservation --------------------------------------------------------

    public static Reservation getFromDTO(TransportProto.Reservation reservationDTO) {
        if (reservationDTO == null) return null;
        Client client = getFromDTO(reservationDTO.getClient());
        Trip trip = getFromDTO(reservationDTO.getTrip());
        int nrReservedSeats = reservationDTO.getNrReservedSeats();
        int id = reservationDTO.getId();
        Reservation reservation = new Reservation(client, trip, nrReservedSeats);
        reservation.setId(id);
        return reservation;
    }

    public static TransportProto.Reservation getDTO(Reservation reservation) {
        if (reservation == null) return null;
        return TransportProto.Reservation.newBuilder()
                .setClient(getDTO(reservation.getClient()))
                .setTrip(getDTO(reservation.getTrip()))
                .setNrReservedSeats(reservation.getNrReservedSeats())
                .setId(reservation.getId())
                .build();
    }

    // --- Array Converters ---------------------------------------------------

    public static TransportProto.Trip[] getDTO(Trip[] trips) {
        if (trips == null) return new TransportProto.Trip[0];
        TransportProto.Trip[] dtos = new TransportProto.Trip[trips.length];
        for (int i = 0; i < trips.length; i++) {
            dtos[i] = getDTO(trips[i]);
        }
        return dtos;
    }

    public static Trip[] getFromDTO(TransportProto.Trip[] tripDTOs) {
        if (tripDTOs == null) return new Trip[0];
        Trip[] trips = new Trip[tripDTOs.length];
        for (int i = 0; i < tripDTOs.length; i++) {
            trips[i] = getFromDTO(tripDTOs[i]);
        }
        return trips;
    }

    public static TransportProto.Reservation[] getDTO(Reservation[] reservations) {
        if (reservations == null) return new TransportProto.Reservation[0];
        TransportProto.Reservation[] dtos = new TransportProto.Reservation[reservations.length];
        for (int i = 0; i < reservations.length; i++) {
            dtos[i] = getDTO(reservations[i]);
        }
        return dtos;
    }

    public static Reservation[] getFromDTO(TransportProto.Reservation[] reservationDTOs) {
        if (reservationDTOs == null) return new Reservation[0];
        Reservation[] reservations = new Reservation[reservationDTOs.length];
        for (int i = 0; i < reservationDTOs.length; i++) {
            reservations[i] = getFromDTO(reservationDTOs[i]);
        }
        return reservations;
    }

}


