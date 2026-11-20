package org.example;

import org.example.Domain.Trip;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public class TripClient {

    private static final String BASE_URL = "http://localhost:8080/api/trips";
    private static final RestClient restClient = RestClient.create();

    public static void main(String[] args) {
        try {
            testAddTrip();
            testGetAllTrips();
            testGetTripById(10);
            testUpdateTrip(27);
            testDeleteTrip(46);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void testAddTrip() throws URISyntaxException {
        Trip trip = new Trip("Destinatie", LocalDateTime.now(), 18);

        try {
            Long newTripId = restClient
                    .post()
                    .uri(new URI(BASE_URL))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(trip)
                    .retrieve()
                    .body(Long.class);

            System.out.println("POST /api/trips ");
            System.out.println("New Trip ID: " + newTripId);

        } catch (RestClientResponseException e) {
            System.out.println("POST /api/trips - Status: " + e.getRawStatusCode());
            System.out.println("Error: " + e.getResponseBodyAsString());
        }
    }

    public static void testGetAllTrips() throws URISyntaxException {
        try {
            List<Trip> trips = restClient
                    .get()
                    .uri(new URI(BASE_URL))
                    .retrieve()
                    .body(List.class);

            System.out.println("GET /api/trips ");
            System.out.println("Response: " + trips);

        } catch (RestClientResponseException e) {
            System.out.println("GET /api/trips - Status: " + e.getRawStatusCode());
            System.out.println("Error: " + e.getResponseBodyAsString());
        }
    }

    public static void testGetTripById(Integer id) throws URISyntaxException {
        try {
            Trip trip = restClient
                    .get()
                    .uri(new URI(BASE_URL + "/" + id))
                    .retrieve()
                    .body(Trip.class);

            System.out.println("GET /api/trips/" + id );
            System.out.println("Trip: " + trip);

        } catch (RestClientResponseException e) {
            System.out.println("GET /api/trips/" + id + " - Status: " + e.getRawStatusCode());
            System.out.println("Error: " + e.getResponseBodyAsString());
        }
    }

    public static void testUpdateTrip(Integer id) throws URISyntaxException {
        Trip trip = new Trip("Destinatie Updated", LocalDateTime.now(), 18);
        trip.setId(id);

        try {
            restClient
                    .put()
                    .uri(new URI(BASE_URL + "/" + id))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(trip)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("PUT /api/trips/" + id );
            System.out.println("Updated Trip: " + trip);

        } catch (RestClientResponseException e) {
            System.out.println("PUT /api/trips/" + id + " - Status: " + e.getRawStatusCode());
            System.out.println("Error: " + e.getResponseBodyAsString());
        }
    }

    public static void testDeleteTrip(Integer id) throws URISyntaxException {
        try {
            restClient
                    .delete()
                    .uri(new URI(BASE_URL + "/" + id))
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("DELETE /api/trips/" + id );
            System.out.println("Trip " + id + " deleted successfully.");

        } catch (RestClientResponseException e) {
            System.out.println("DELETE /api/trips/" + id + " - Status: " + e.getRawStatusCode());
            System.out.println("Error: " + e.getResponseBodyAsString());
        }
    }
}

