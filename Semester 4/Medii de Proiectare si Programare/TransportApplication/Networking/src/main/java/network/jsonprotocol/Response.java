package network.jsonprotocol;

import network.dto.AdminDTO;
import network.dto.ClientDTO;
import network.dto.ReservationDTO;
import network.dto.TripDTO;

public class Response {

    private ResponseType type;
    private String errorMessage;
    private AdminDTO admin;
    private ClientDTO client;
    private TripDTO trip;
    private ReservationDTO reservation;
    private TripDTO[] listedTrips;
    private ReservationDTO[] listedReservations;

    public Response() {}

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public ReservationDTO getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDTO reservation) {
        this.reservation = reservation;
    }

    public TripDTO[] getListedTrips() {
        return listedTrips;
    }

    public void setListedTrips(TripDTO[] listedTrips) {
        this.listedTrips = listedTrips;
    }

    public ReservationDTO[] getListedReservations() {
        return listedReservations;
    }

    public void setListedReservations(ReservationDTO[] listedReservations) {
        this.listedReservations = listedReservations;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", admin=" + admin +
                ", client=" + client +
                ", trip=" + trip +
                ", reservation=" + reservation +
                '}';
    }
}



