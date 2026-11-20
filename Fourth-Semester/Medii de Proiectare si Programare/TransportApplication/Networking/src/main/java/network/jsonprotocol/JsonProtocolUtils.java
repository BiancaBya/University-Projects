package network.jsonprotocol;

import network.dto.DTOUtils;
import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;

public class JsonProtocolUtils {


    public static Response createOkResponse(){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage){
        Response resp=new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }


    public static Response createGetAllTripsResponse(Trip[] trips){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        resp.setListedTrips(DTOUtils.getDTO(trips));
        return resp;
    }

    public static Request createGetAllTripsRequest(){
        Request req=new Request();
        req.setType(RequestType.GET_ALL_TRIPS);
        return req;
    }


    public static Response createGetAllReservationsResponse(Reservation[] reservations){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        resp.setListedReservations(DTOUtils.getDTO(reservations));
        return resp;
    }

    public static Request createGetAllReservationsRequest(){
        Request req=new Request();
        req.setType(RequestType.GET_ALL_RESERVATIONS);
        return req;
    }


    public static Response createGetReservationsForTripResponse(Reservation[] reservations){
        Response resp=new Response();
        resp.setType(ResponseType.GET_RESERVATIONS_FOR_TRIP);
        resp.setListedReservations(DTOUtils.getDTO(reservations));
        return resp;
    }

    public static Request createGetReservationsForTripRequest(Trip trip){
        Request req=new Request();
        req.setType(RequestType.GET_RESERVATIONS_FOR_TRIP);
        req.setTrip(DTOUtils.getDTO(trip));
        return req;
    }



    public static Request createGetTripByTimeAndDestinationRequest(Trip trip){
        Request req=new Request();
        req.setType(RequestType.GET_TRIP_BY_DESTINATION_AND_TIME);
        req.setTrip(DTOUtils.getDTO(trip));
        return req;
    }

    public static Response createGetTripByTimeAndDestinationResponse(Trip trip){
        Response resp=new Response();
        resp.setType(ResponseType.GET_TRIP_BY_DESTINATION_AND_TIME);
        resp.setTrip(DTOUtils.getDTO(trip));
        return resp;
    }



    public static Request createFindClientByNameRequest(Client client){
        Request req=new Request();
        req.setType(RequestType.FIND_CLIENT_BY_NAME);
        req.setClient(DTOUtils.getDTO(client));
        return req;
    }

    public static Response createFindClientByNameResponse(Client client){
        Response resp=new Response();
        resp.setType(ResponseType.FIND_CLIENT_BY_NAME);
        resp.setClient(DTOUtils.getDTO(client));
        return resp;
    }


    public static Request createSaveClientRequest(Client client){
        Request req=new Request();
        req.setType(RequestType.SAVE_CLIENT);
        req.setClient(DTOUtils.getDTO(client));
        return req;
    }

    public static Response createSaveClientResponse(Client client){
        Response resp=new Response();
        resp.setType(ResponseType.SAVE_CLIENT);
        resp.setClient(DTOUtils.getDTO(client));
        return resp;
    }


    public static Request createSaveReservationRequest(Reservation reservation){
        Request req=new Request();
        req.setType(RequestType.SAVE_RESERVATION);
        req.setReservation(DTOUtils.getDTO(reservation));
        return req;
    }

    public static Response createSaveReservationResponse(Reservation reservation){
        Response resp=new Response();
        resp.setType(ResponseType.SAVE_RESERVATION);
        resp.setReservation(DTOUtils.getDTO(reservation));
        return resp;
    }



    public static Request createLoginRequest(Admin admin){
        Request req=new Request();
        req.setType(RequestType.LOGIN);
        req.setAdmin(DTOUtils.getDTO(admin));
        return req;
    }


    public static Request createLogoutRequest(Admin admin){
        Request req=new Request();
        req.setType(RequestType.LOGOUT);
        req.setAdmin(DTOUtils.getDTO(admin));
        return req;
    }

    public static Response createReservationAddedResponse(Reservation reservation){
        Response resp=new Response();
        resp.setType(ResponseType.UPDATE_RESERVATION_ADDED);
        resp.setReservation(DTOUtils.getDTO(reservation));
        return resp;
    }

}



