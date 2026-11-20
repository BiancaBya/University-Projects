package network.grpcprotocol;

import org.example.Domain.*;
import services.IObserver;
import services.IServices;
import services.ServiceException;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;

public class TClientGrpcWorker implements StreamObserver<TransportProto.TransportRequest>, IObserver {
    private final IServices server;
    private final StreamObserver<TransportProto.TransportResponse> responseObserver;

    public TClientGrpcWorker(IServices server, StreamObserver<TransportProto.TransportResponse> responseObserver) {
        this.server = server;
        this.responseObserver = responseObserver;
    }

    private void handleLogin(TransportProto.TransportRequest request){
        try{
            Admin admin = GrpcDtoUtils.getFromDTO(request.getAdmin());
            server.loginAdmin(admin, this);
            responseObserver.onNext(GrpcProtoUtils.createOkResponse());
        }
        catch (ServiceException e){
            e.printStackTrace();
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleLogout(TransportProto.TransportRequest request) {
        try {
            Admin admin = GrpcDtoUtils.getFromDTO(request.getAdmin());
            server.logout(admin);
            responseObserver.onNext(GrpcProtoUtils.createOkResponse());
            responseObserver.onCompleted();
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleGetAllTrips(TransportProto.TransportRequest request) {
        try {
            Trip[] trips = server.findAllTrips();
            TransportProto.TransportResponse response = GrpcProtoUtils.createGetAllTripsResponse(trips);
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleGetReservationsForTrip(TransportProto.TransportRequest request) {
        try {
            TransportProto.Trip tripDto = request.getTrip();
            Trip trip = GrpcDtoUtils.getFromDTO(tripDto);
            Reservation[] reservations = server.getReservationsForTrip(trip);
            TransportProto.TransportResponse response = GrpcProtoUtils.createGetReservationsForTripResponse(reservations);
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleGetTripByDestinationAndTime(TransportProto.TransportRequest request) {
        try {
            TransportProto.Trip tripDto = request.getTrip();
            Trip criteria = GrpcDtoUtils.getFromDTO(tripDto);
            Trip trip = server.findTripByTimeAndDestination(criteria.getDate(), criteria.getDestination());
            TransportProto.TransportResponse response = TransportProto.TransportResponse.newBuilder().build();
            if (trip == null) {
                response = GrpcProtoUtils.createErrorResponse("Trip not found");
            } else {
                response = GrpcProtoUtils.createGetTripByDestinationAndTimeResponse(trip);
            }
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleGetAllReservations(TransportProto.TransportRequest request) {
        try {
            Reservation[] reservations = server.findAllReservations();
            TransportProto.TransportResponse response = GrpcProtoUtils.createGetAllReservationsResponse(reservations);
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleFindClientByName(TransportProto.TransportRequest request) {
        try {
            TransportProto.Client clientDto = request.getClient();
            Client client = GrpcDtoUtils.getFromDTO(clientDto);
            Client result = server.findClientByNames(client.getFirstName(), client.getLastName());
            TransportProto.TransportResponse response = TransportProto.TransportResponse.newBuilder().build();
            if (result == null) {
                response = GrpcProtoUtils.createErrorResponse("Client not found");
            } else {
                response = GrpcProtoUtils.createFindClientByNameResponse(result);
            }
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleSaveClient(TransportProto.TransportRequest request) {
        try {
            Client client = GrpcDtoUtils.getFromDTO(request.getClient());
            Client saved = server.saveClient(client);
            TransportProto.TransportResponse response = GrpcProtoUtils.createSaveClientResponse(saved);
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }

    private void handleSaveReservation(TransportProto.TransportRequest request) {
        try {
            Reservation res = GrpcDtoUtils.getFromDTO(request.getReservation());
            Reservation saved = server.saveReservation(res);
            TransportProto.TransportResponse response = GrpcProtoUtils.createSaveReservationResponse(saved);
            responseObserver.onNext(response);
        } catch (ServiceException e) {
            responseObserver.onNext(GrpcProtoUtils.createErrorResponse(e.getMessage()));
        }
    }




    @Override
    public void onNext(TransportProto.TransportRequest request) {
        switch (request.getType()) {
            case LOGIN:
                handleLogin(request);
                break;
            case LOGOUT:
                handleLogout(request);
                break;
            case GET_ALL_TRIPS_REQ:
                handleGetAllTrips(request);
                break;
            case GET_RESERVATIONS_FOR_TRIP_REQ:
                handleGetReservationsForTrip(request);
                break;
            case GET_TRIP_BY_DESTINATION_AND_TIME_REQ:
                handleGetTripByDestinationAndTime(request);
                break;
            case GET_ALL_RESERVATIONS_REQ:
                handleGetAllReservations(request);
                break;
            case FIND_CLIENT_BY_NAME_REQ:
                handleFindClientByName(request);
                break;
            case SAVE_CLIENT_REQ:
                handleSaveClient(request);
                break;
            case SAVE_RESERVATION_REQ:
                handleSaveReservation(request);
                break;
            default:
                responseObserver.onNext(GrpcProtoUtils.createErrorResponse("Unknown request type: " + request.getType()));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Client disconnected with error: " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }

    @Override
    public void reservationAdded(Reservation reservation) throws ServiceException {
        TransportProto.TransportResponse response =
                GrpcProtoUtils.createReservationAddedResponse(reservation);
        responseObserver.onNext(response);
    }
}
