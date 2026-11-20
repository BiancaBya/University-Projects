package network.dto;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;

public class DTOUtils {

    public static Admin getFromDTO(AdminDTO adminDTO){
        return new Admin(adminDTO.getEmail(), adminDTO.getPassword());
    }

    public static AdminDTO getDTO(Admin admin){
        return new AdminDTO(admin.getEmail(),admin.getPassword());
    }

    public static Client getFromDTO(ClientDTO clientDTO){
        if(clientDTO == null)
            return null;
        Client client = new Client(clientDTO.getFirstName(), clientDTO.getLastName(), clientDTO.getEmail(), clientDTO.getPhone());
        client.setId(clientDTO.getId_client());
        return client;
    }

    public static ClientDTO getDTO(Client client){
        if(client == null)
            return null;
        ClientDTO clientDTO = new ClientDTO(client.getFirstName(), client.getLastName(), client.getEmail(), client.getPhone());
        clientDTO.setId_client(client.getId());
        return clientDTO;
    }

    public static Reservation getFromDTO(ReservationDTO reservationDTO){
        return new Reservation(reservationDTO.getClient(), reservationDTO.getTrip(), reservationDTO.getNrReservedSeats());
    }

    public static ReservationDTO getDTO(Reservation reservation){
        return new ReservationDTO(reservation.getClient(), reservation.getTrip(), reservation.getNrReservedSeats());
    }

    public static Trip getFromDTO(TripDTO tripDTO){
        if(tripDTO == null)
            return null;
        Trip trip = new Trip(tripDTO.getDestination(), tripDTO.getDate(), tripDTO.getNrSeats());
        trip.setId(tripDTO.getId_trip());
        return trip;
    }

    public static TripDTO getDTO(Trip trip){
        if(trip == null)
            return null;
        TripDTO tripDTO = new TripDTO(trip.getDestination(), trip.getDate(), trip.getNrSeats());
        tripDTO.setId_trip(trip.getId());
        return tripDTO;
    }

    public static TripDTO[] getDTO(Trip[] trips){
        TripDTO[] dtos = new TripDTO[trips.length];
        for (int i = 0; i < trips.length; i++) {
            dtos[i] = getDTO(trips[i]);
        }
        return dtos;
    }

    public static Trip[] getFromDTO(TripDTO[] tripDTOs){
        Trip[] trips = new Trip[tripDTOs.length];
        for (int i = 0; i < tripDTOs.length; i++) {
            trips[i] = getFromDTO(tripDTOs[i]);
        }
        return trips;
    }

    public static ReservationDTO[] getDTO(Reservation[] reservations){
        ReservationDTO[] dtos = new ReservationDTO[reservations.length];
        for (int i = 0; i < reservations.length; i++) {
            dtos[i] = getDTO(reservations[i]);
        }
        return dtos;
    }

    public static Reservation[] getFromDTO(ReservationDTO[] reservationDTOs){
        Reservation[] reservations = new Reservation[reservationDTOs.length];
        for (int i = 0; i < reservationDTOs.length; i++) {
            reservations[i] = getFromDTO(reservationDTOs[i]);
        }
        return reservations;
    }
}


