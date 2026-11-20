package org.example.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Seat;
import org.example.Domain.Trip;
import org.example.Service.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MainController {

    private Service service;

    ObservableList<Trip> modelList = FXCollections.observableArrayList();

    ObservableList<Seat> modelTable = FXCollections.observableArrayList();

    @FXML
    ListView<Trip> listTrips = new ListView<>();

    @FXML
    Button searchBtn;

    @FXML
    DatePicker datePicker;

    @FXML
    ComboBox<Integer> hourComboBox;

    @FXML
    ComboBox<Integer> minutesComboBox;

    @FXML
    TextField destinationField;

    @FXML
    TableView<Seat> tableSeats = new TableView<>();

    @FXML
    Label messageLabel;

    @FXML
    TableColumn<Seat, String> clientNameColumn;

    @FXML
    TableColumn<Seat, Integer> numSeatColumn;

    @FXML
    TextField clientBookNameField;

    @FXML
    TextField clientBookEmailField;

    @FXML
    TextField clientBookPhoneField;

    @FXML
    TextField nrSeatsBookedField;

    private Trip selectedTrip = null;

    int totalNrAvailableSeats = 18;



    public void setService(Service service) {
        this.service = service;
        initModel();
    }

    private void initModel(){

        for (Trip trip : service.findAllTrips()){
            int nrBookedSeats = 0;
            for (Reservation reservation : service.findAllReservations()){
                if (reservation.getTrip().equals(trip)){
                    nrBookedSeats += reservation.getNrReservedSeats();
                }
            }
            trip.setNrSeats(totalNrAvailableSeats - nrBookedSeats);
            service.updateTrip(trip);
        }

        modelList.setAll(service.findAllTrips());
    }

    @FXML
    private void initialize() {

        listTrips.setItems(modelList);
        tableSeats.setItems(modelTable);

        ObservableList<Integer> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        hourComboBox.setItems(hours);
        hourComboBox.getSelectionModel().select(0);

        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            minutes.add(i);
        }
        minutesComboBox.setItems(minutes);
        minutesComboBox.getSelectionModel().select(0);

        listTrips.setOnMouseClicked(event -> {
            Trip selected = listTrips.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedTrip = selected;
            }
        });

    }

    public void onBtnSearchClicked(){

        if(datePicker.getValue() != null && hourComboBox.getValue() != null && minutesComboBox.getValue() != null && destinationField.getText() != null){

            LocalDate date = datePicker.getValue();
            Integer hour = hourComboBox.getValue();
            Integer minute = minutesComboBox.getValue();

            LocalDateTime time = LocalDateTime.of(date, java.time.LocalTime.of(hour, minute));
            String destination = destinationField.getText();
            if(service.findTripByTimeAndDestination(time, destination) != null){
                messageLabel.setText("Trip found");
                Trip trip = service.findTripByTimeAndDestination(time, destination);
                addReservationColumns(trip);
            }
            else{
                messageLabel.setText("No trip found");
            }

        }
        else{
            messageLabel.setText("Please select the values");
        }
    }

    private void addReservationColumns(Trip trip) {

        List<Reservation> reservations = service.getReservationsForTrip(trip);
        List<Seat> seats = new ArrayList<>();
        Map<Client, Integer> clientsSeats = new HashMap<>();

        int reservedSeats = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getTrip().equals(trip)) {
                clientsSeats.put(reservation.getClient(), reservation.getNrReservedSeats());
                reservedSeats += reservation.getNrReservedSeats();
            }
        }

        int leftSeats = trip.getNrSeats() - reservedSeats;
        int seatNr = 1;

        for (Map.Entry<Client, Integer> entry : clientsSeats.entrySet()) {
            Client client = entry.getKey();
            int nrSeats = entry.getValue();
            for (int i = 1; i <= nrSeats; i++) {
                seats.add(new Seat(seatNr, client.getFirstName() + " " + client.getLastName()));
                seatNr++;
            }
        }

        while (leftSeats > 0) {
            seats.add(new Seat(seatNr, "-"));
            seatNr++;
            leftSeats--;
        }

        ObservableList<Seat> seatData = FXCollections.observableArrayList(seats);
        tableSeats.setItems(seatData);

        numSeatColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeatNumber()).asObject());

        clientNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientName()));

    }

    public void onBtnBookClicked(){

        if(clientBookNameField.getText() != null && nrSeatsBookedField.getText() != null && selectedTrip != null){
            int reservedSeats = Integer.parseInt(nrSeatsBookedField.getText());
            String clientName = clientBookNameField.getText().trim();
            String[] names = clientName.split(" ");
            if (names.length == 2) {
                String firstName = names[0];
                String lastName = names[1];
                if (service.findClientByNames(firstName, lastName) != null) {

                    Client client = service.findClientByNames(firstName, lastName);
                    Reservation reservation = new Reservation(client, selectedTrip, reservedSeats);
                    Optional<Reservation> saved = service.saveReservation(reservation);
                    if (saved.isEmpty()) {
                        messageLabel.setText("Reservation saved");
                        initModel();
                    } else {
                        messageLabel.setText("Reservation not saved");
                    }

                } else {
                    String email = clientBookEmailField.getText().trim();
                    String phone = clientBookPhoneField.getText().trim();
                    Client client = new Client(firstName, lastName, email, phone);
                    service.saveClient(client);

                    client = service.findClientByNames(firstName, lastName);
                    Reservation reservation = new Reservation(client, selectedTrip, reservedSeats);
                    Optional<Reservation> saved = service.saveReservation(reservation);
                    if (saved.isEmpty()) {
                        messageLabel.setText("Reservation saved");
                        initModel();
                    } else {
                        messageLabel.setText("Reservation not saved");
                    }
                }
            }
            else{
                messageLabel.setText("Please insert your first name and last name");
            }
        }
        else{
            messageLabel.setText("Please select the client, nr of seats and the trip");
        }

    }


    public void onBtnLogOutClicked() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setScene(scene);

        LoginController controller = fxmlLoader.getController();
        controller.setService(service);

        stage.show();

    }
}


