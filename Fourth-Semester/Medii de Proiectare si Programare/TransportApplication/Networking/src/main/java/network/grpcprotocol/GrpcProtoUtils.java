package network.grpcprotocol;

import org.example.Domain.*;
import network.grpcprotocol.TransportProto.ResponseType;
import network.grpcprotocol.TransportProto.TripsList;
import network.grpcprotocol.TransportProto.ReservationsList;

import java.util.Arrays;

public class GrpcProtoUtils {

    public static TransportProto.TransportResponse createOkResponse() {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.OK)
                .build();
    }

    public static TransportProto.TransportResponse createErrorResponse(String message) {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.ERROR)
                .setErrorMessage(message)
                .build();
    }

    public static TransportProto.TransportResponse createGetAllTripsResponse(Trip[] trips) {
        TripsList tripsList = TripsList.newBuilder()
                .addAllTrips(Arrays.asList(GrpcDtoUtils.getDTO(trips)))
                .build();
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.OK)
                .setTrips(tripsList)
                .build();
    }

    public static TransportProto.TransportResponse createGetAllReservationsResponse(Reservation[] reservations) {
        ReservationsList list = ReservationsList.newBuilder()
                .addAllReservations(Arrays.asList(GrpcDtoUtils.getDTO(reservations)))
                .build();
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.GET_ALL_RESERVATIONS_RESP)
                .setReservations(list)
                .build();
    }

    public static TransportProto.TransportResponse createGetReservationsForTripResponse(Reservation[] reservations) {
        ReservationsList list = ReservationsList.newBuilder()
                .addAllReservations(Arrays.asList(GrpcDtoUtils.getDTO(reservations)))
                .build();
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.GET_RESERVATIONS_FOR_TRIP_RESP)
                .setReservations(list)
                .build();
    }

    public static TransportProto.TransportResponse createGetTripByDestinationAndTimeResponse(Trip trip) {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.GET_TRIP_BY_DESTINATION_AND_TIME_RESP)
                .setTrip(GrpcDtoUtils.getDTO(trip))
                .build();
    }

    public static TransportProto.TransportResponse createFindClientByNameResponse(Client client) {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.FIND_CLIENT_BY_NAME_RESP)
                .setClient(GrpcDtoUtils.getDTO(client))
                .build();
    }

    public static TransportProto.TransportResponse createSaveClientResponse(Client client) {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.SAVE_CLIENT_RESP)
                .setClient(GrpcDtoUtils.getDTO(client))
                .build();
    }

    public static TransportProto.TransportResponse createSaveReservationResponse(Reservation reservation) {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.SAVE_RESERVATION_RESP)
                .setReservation(GrpcDtoUtils.getDTO(reservation))
                .build();
    }

    public static TransportProto.TransportResponse createReservationAddedResponse(Reservation reservation) {
        return TransportProto.TransportResponse.newBuilder()
                .setType(ResponseType.UPDATE_RESERVATION_ADDED_RESP)
                .setReservation(GrpcDtoUtils.getDTO(reservation))
                .build();
    }
}
