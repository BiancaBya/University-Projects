package network.jsonprotocol;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import network.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientJsonWorker implements Runnable, IObserver {

    private IServices server;

    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;

    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ClientJsonWorker.class);

    private Gson gsonFormatter = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                    LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, context) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();;


    public ClientJsonWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new PrintWriter(connection.getOutputStream());
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected=true;
        }catch (IOException e){
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }


    @Override
    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }


    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request){

        Response response=null;
        if (request.getType()== RequestType.LOGIN){
            logger.debug("Login request ...{}"+request.getAdmin());
            AdminDTO adminDTO = request.getAdmin();
            Admin admin = DTOUtils.getFromDTO(adminDTO);
            try {
                server.loginAdmin(admin, this);
                return okResponse;
            } catch (ServiceException e) {
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType()== RequestType.LOGOUT) {
            logger.debug("Logout request {}", request.getAdmin());
            AdminDTO adminDTO = request.getAdmin();
            Admin admin = DTOUtils.getFromDTO(adminDTO);
            try {
                server.logout(admin);
                connected = false;
                return okResponse;
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType()==RequestType.GET_ALL_TRIPS){
            try{
                Trip[] listedTrips = server.findAllTrips();
                return JsonProtocolUtils.createGetAllTripsResponse(listedTrips);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType()==RequestType.GET_ALL_RESERVATIONS){
            try{
                Reservation[] listedReservations = server.findAllReservations();
                return JsonProtocolUtils.createGetAllReservationsResponse(listedReservations);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()==RequestType.GET_RESERVATIONS_FOR_TRIP){
            TripDTO tripDTO = request.getTrip();
            Trip trip = DTOUtils.getFromDTO(tripDTO);
            try{
                Reservation[] listedReservations = server.getReservationsForTrip(trip);
                return JsonProtocolUtils.createGetReservationsForTripResponse(listedReservations);
            }catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()==RequestType.GET_TRIP_BY_DESTINATION_AND_TIME){
            TripDTO tripDTO = request.getTrip();
            Trip trip = DTOUtils.getFromDTO(tripDTO);
            try{
                Trip returnedTrip = server.findTripByTimeAndDestination(trip.getDate(), trip.getDestination());
                return JsonProtocolUtils.createGetTripByTimeAndDestinationResponse(returnedTrip);
            }catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()==RequestType.FIND_CLIENT_BY_NAME){
            ClientDTO clientDTO = request.getClient();
            Client client = DTOUtils.getFromDTO(clientDTO);
            try{
                Client returnedCliend = server.findClientByNames(client.getFirstName(), client.getLastName());
                return JsonProtocolUtils.createFindClientByNameResponse(returnedCliend);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()==RequestType.SAVE_CLIENT){
            ClientDTO clientDTO = request.getClient();
            Client client = DTOUtils.getFromDTO(clientDTO);
            try{
                Client savedClient = server.saveClient(client);
                return JsonProtocolUtils.createSaveClientResponse(savedClient);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()==RequestType.SAVE_RESERVATION){
            ReservationDTO reservationDTO = request.getReservation();
            Reservation reservation = DTOUtils.getFromDTO(reservationDTO);
            try{
                Reservation savedReservation = server.saveReservation(reservation);
                return JsonProtocolUtils.createSaveReservationResponse(savedReservation);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    public void reservationAdded(Reservation reservation) throws ServiceException{
        Response resp = JsonProtocolUtils.createReservationAddedResponse(reservation);
        logger.debug("reservationAdded response {}", resp);
        try {
            sendResponse(resp);
        } catch (IOException e){
            logger.error("Error saving reservation {}", reservation);
            logger.error(e.getMessage());
        }
    }


    private void sendResponse(Response response) throws IOException{
        String responseLine=gsonFormatter.toJson(response);
        logger.debug("sending response {}", responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }
}



