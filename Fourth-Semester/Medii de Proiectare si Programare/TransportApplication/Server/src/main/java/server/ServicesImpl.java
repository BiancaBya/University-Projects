package server;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;
import org.example.Repository.AdminRepoInterface;
import org.example.Repository.ClientRepoInterface;
import org.example.Repository.ReservationRepoInterface;
import org.example.Repository.TripRepoInterface;
import services.IObserver;
import services.IServices;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ServiceException;

public class ServicesImpl implements IServices {

    private final AdminRepoInterface adminRepo;;
    private final ClientRepoInterface clientRepo;
    private final TripRepoInterface tripRepo;
    private final ReservationRepoInterface reservationRepo;
    private Map<String, IObserver> loggedAdmins;
    private static Logger logger = LogManager.getLogger(ServicesImpl.class);

    public ServicesImpl(AdminRepoInterface adminRepo, ClientRepoInterface clientRepo, TripRepoInterface tripRepo, ReservationRepoInterface reservationRepo) {
        this.adminRepo = adminRepo;
        this.clientRepo = clientRepo;
        this.tripRepo = tripRepo;
        this.reservationRepo = reservationRepo;
        this.loggedAdmins = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized void loginAdmin(Admin admin, IObserver client) throws ServiceException {
        for (Admin a : adminRepo.findAll()) {
            if (admin.getEmail().equals(a.getEmail())) {
                if (loggedAdmins.containsKey(admin.getEmail())) {
                    throw new ServiceException("Admin already logged in");
                }
                loggedAdmins.put(admin.getEmail(), client);
                return;
            }
        }
        throw  new ServiceException("The admin does not exist!");
    }

    @Override
    public synchronized void logout(Admin admin) throws ServiceException {
        IObserver localClient=loggedAdmins.remove(admin.getEmail());
        if (localClient==null)
            throw new ServiceException("Admin "+admin.getId()+" is not logged in.");
    }

    @Override
    public Trip[] findAllTrips() throws ServiceException{
        List<Trip> trips = new ArrayList<>();
        tripRepo.findAll().forEach(trips::add);
        return trips.toArray(new Trip[0]);
    }

    @Override
    public Reservation[] getReservationsForTrip(Trip trip) throws ServiceException {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : reservationRepo.findAll()) {
            if (reservation.getTrip().getDestination().equals(trip.getDestination()) && reservation.getTrip().getDate().equals(trip.getDate())) {
                reservations.add(reservation);
            }
        }
        return reservations.toArray(new Reservation[0]);
    }

    @Override
    public Trip findTripByTimeAndDestination(LocalDateTime time, String destination) throws ServiceException{
        LocalDateTime searchTime = time.truncatedTo(ChronoUnit.DAYS);

        for (Trip trip : tripRepo.findAll()) {
            LocalDateTime tripTime = trip.getDate().truncatedTo(ChronoUnit.DAYS);
            if (trip.getDestination().equals(destination)
                    && tripTime.equals(searchTime)) {
                return trip;
            }
        }
        return null;
    }

    @Override
    public Reservation[] findAllReservations() throws ServiceException{
        List<Reservation> reservations = new ArrayList<>();
        reservationRepo.findAll().forEach(reservations::add);
        return reservations.toArray(new Reservation[0]);
    }

    @Override
    public Client findClientByNames(String firstName, String lastName) throws ServiceException {
        for (Client client : clientRepo.findAll()) {
            if (client.getFirstName().equals(firstName) && client.getLastName().equals(lastName)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public synchronized Client saveClient(Client client) throws ServiceException {
        client.setId(null);
        if(clientRepo.save(client).isEmpty()){
            return client;
        }
        throw new ServiceException("Clientul not saved");
    }

    @Override
    public synchronized Reservation saveReservation(Reservation reservation) throws ServiceException {
        if (reservationRepo.save(reservation).isEmpty()){
            notifyObservers(reservation);
            return reservation;
        }
        throw new ServiceException("Reservation not saved");
    }


    private final int defaultThreadsNo=3;
    private void notifyObservers(Reservation reservation) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver observer : loggedAdmins.values()){
            if (observer!=null)
                executor.execute(() -> {
                    try {
                        logger.debug("Notifying observer {}", observer);
                        observer.reservationAdded(reservation);
                    } catch (ServiceException e) {
                        logger.error("Error notifying friend {}", String.valueOf(e));
                    }
                });

        }
        executor.shutdown();
    }

}



