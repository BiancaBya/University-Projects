package network.jsonprotocol;

import network.dto.DTOUtils;
import network.dto.TripDTO;
import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import com.google.gson.Gson;
import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServicesJsonProxy implements IServices {

    private String host;
    private int port;

    private IObserver clientObserver;

    private BufferedReader input;
    private PrintWriter output;

    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    private static Logger logger = LogManager.getLogger(ServicesJsonProxy.class);

    private Gson gsonFormatter = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                    LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, context) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();


    public ServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }


    @Override
    public void loginAdmin(Admin admin, IObserver client) throws ServiceException {
        initializeConnection();
        Request req = JsonProtocolUtils.createLoginRequest(admin);
        sendRequest(req);
        Response response = readResponse();

        if(response.getType() == ResponseType.OK){
            this.clientObserver = client;
        }

        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            closeConnection();
            throw new ServiceException(err);
        }
    }

    @Override
    public void logout(Admin admin) throws ServiceException{
        Request req = JsonProtocolUtils.createLogoutRequest(admin);
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
    }

    @Override
    public Trip[] findAllTrips() throws ServiceException{
        Request req=JsonProtocolUtils.createGetAllTripsRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        TripDTO[] tripsDTO = response.getListedTrips();
        Trip[] listedTrips = DTOUtils.getFromDTO(tripsDTO);
        return listedTrips;
    }

    @Override
    public Reservation[] findAllReservations() throws ServiceException{
        Request req=JsonProtocolUtils.createGetAllReservationsRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        Reservation[] reservations = DTOUtils.getFromDTO(response.getListedReservations());
        return reservations;
    }

    @Override
    public Reservation[] getReservationsForTrip(Trip trip) throws ServiceException{
        Request req=JsonProtocolUtils.createGetReservationsForTripRequest(trip);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        Reservation[] reservations = DTOUtils.getFromDTO(response.getListedReservations());
        return reservations;
    }

    @Override
    public Trip findTripByTimeAndDestination(LocalDateTime time, String destination) throws ServiceException{
        Trip trip = new Trip(destination, time, -1);
        trip.setId(0);
        Request req=JsonProtocolUtils.createGetTripByTimeAndDestinationRequest(trip);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        Trip returnedTrip = DTOUtils.getFromDTO(response.getTrip());
        return returnedTrip;
    }

    @Override
    public Client findClientByNames(String firstName, String lastName) throws ServiceException{
        Client client = new Client(firstName, lastName, "", "");
        client.setId(0);
        Request req=JsonProtocolUtils.createFindClientByNameRequest(client);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        return DTOUtils.getFromDTO(response.getClient());
    }

    @Override
    public Client saveClient(Client client) throws ServiceException{
        Request req=JsonProtocolUtils.createSaveClientRequest(client);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        return DTOUtils.getFromDTO(response.getClient());
    }

    @Override
    public Reservation saveReservation(Reservation reservation) throws ServiceException{
        Request req=JsonProtocolUtils.createSaveReservationRequest(reservation);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        return DTOUtils.getFromDTO(response.getReservation());
    }


    private void initializeConnection() throws ServiceException {
        try {
            gsonFormatter= new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                            LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, context) ->
                            new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .create();
            connection=new Socket(host,port);
            output=new PrintWriter(connection.getOutputStream());
            output.flush();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished=false;
            startReader();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            clientObserver=null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

    }

    private void sendRequest(Request request)throws ServiceException {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServiceException {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }


    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdate(Response response){
        if(response == null)
            return false;
        return response.getType()== ResponseType.UPDATE_RESERVATION_ADDED;
    }

    private void handleUpdate(Response response){
        if (response.getType()== ResponseType.UPDATE_RESERVATION_ADDED){

            Reservation reservation=DTOUtils.getFromDTO(response.getReservation());
            logger.debug("Reservation added: {}", reservation);
            try {
                clientObserver.reservationAdded(reservation);
            } catch (ServiceException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    String responseLine=input.readLine();
                    logger.debug("response received {}",responseLine);
                    Response response=gsonFormatter.fromJson(responseLine, Response.class);
                    if(response != null) {
                        if (isUpdate(response)) {
                            handleUpdate(response);
                        } else {

                            try {
                                qresponses.put(response);
                            } catch (InterruptedException e) {
                                logger.error(e);
                                logger.error(e.getStackTrace());
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }
}


