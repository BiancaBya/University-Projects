package services;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IServices {
    public void loginAdmin(Admin admin, IObserver client) throws ServiceException;
    public Trip[] findAllTrips() throws ServiceException;
    public Reservation[] getReservationsForTrip(Trip trip) throws ServiceException;
    public Trip findTripByTimeAndDestination(LocalDateTime time, String destination) throws ServiceException;
    public Reservation[] findAllReservations() throws ServiceException;
    public Client findClientByNames(String firstName, String lastName) throws ServiceException;
    public Client saveClient(Client client) throws ServiceException;
    public Reservation saveReservation(Reservation reservation) throws ServiceException;
    public void logout(Admin admin) throws ServiceException;
}



