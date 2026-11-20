package client.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.*;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MainController implements IObserver {

    private IServices server;
    private Admin admin;

    private static Logger logger = LogManager.getLogger(MainController.class);

    public void setServer(IServices s) {
        server = s;
    }

    public void setAdmin(Admin a) {
        admin = a;
    }


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


    public void initModel() {
        Task<List<Trip>> task = new Task<>() {
            @Override
            protected List<Trip> call() throws Exception {
                Trip[] allTrips = server.findAllTrips();
                Reservation[] allReservations = server.findAllReservations();

                List<Trip> updatedTrips = new ArrayList<>();

                for (Trip trip : allTrips) {
                    int nrBookedSeats = 0;
                    for (Reservation reservation : allReservations) {
                        if (reservation.getTrip().getDestination().equals(trip.getDestination()) && reservation.getTrip().getDate().equals(trip.getDate())) {
                            nrBookedSeats += reservation.getNrReservedSeats();
                        }
                    }

                    Trip updatedTrip = new Trip(
                            trip.getDestination(),
                            trip.getDate(),
                            totalNrAvailableSeats - nrBookedSeats
                    );

                    updatedTrips.add(updatedTrip);
                }
                return updatedTrips;
            }

            @Override
            protected void succeeded() {
                modelList.setAll(getValue());
            }
        };

        new Thread(task).start();
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

        listTrips.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    selectedTrip = server.findTripByTimeAndDestination(newVal.getDate(), newVal.getDestination());
                } catch (ServiceException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        listTrips.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Trip trip, boolean empty) {
                super.updateItem(trip, empty);

                if (empty || trip == null) {
                    setText(null);
                } else {
                    setText(trip.getDestination() + " - " + trip.getDate() + " - " + trip.getNrSeats());
                }
            }
        });
    }

    public void onBtnSearchClicked() throws ServiceException {

        if(datePicker.getValue() != null && hourComboBox.getValue() != null && minutesComboBox.getValue() != null && destinationField.getText() != null){

            LocalDate date = datePicker.getValue();
            Integer hour = hourComboBox.getValue();
            Integer minute = minutesComboBox.getValue();

            LocalDateTime time = LocalDateTime.of(date, java.time.LocalTime.of(hour, minute));
            String destination = destinationField.getText();
            if(server.findTripByTimeAndDestination(time, destination) != null){
                messageLabel.setText("Trip found");
                Trip trip = server.findTripByTimeAndDestination(time, destination);
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

    private void addReservationColumns(Trip trip) throws ServiceException {

        Reservation[] reservations = server.getReservationsForTrip(trip);

        List<Seat> seats = new ArrayList<>();
        Map<Client, Integer> clientsSeats = new HashMap<>();

        int reservedSeats = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getTrip().getDestination().equals(trip.getDestination()) && reservation.getTrip().getDate().equals(trip.getDate())) {
                clientsSeats.put(reservation.getClient(), reservation.getNrReservedSeats());
                reservedSeats += reservation.getNrReservedSeats();
            }
        }

        int leftSeats = totalNrAvailableSeats - reservedSeats;
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

    public void onBtnBookClicked() {

        String clientName = clientBookNameField.getText().trim();
        String email = clientBookEmailField.getText().trim();
        String phone = clientBookPhoneField.getText().trim();
        String seatsText = nrSeatsBookedField.getText().trim();

        if (selectedTrip == null || clientName.isEmpty() || seatsText.isEmpty()) {
            messageLabel.setText("Please complete all fields");
            return;
        }

        String[] names = clientName.split(" ");
        if (names.length != 2) {
            messageLabel.setText("Please insert first and last name");
            return;
        }

        int reservedSeats;
        try {
            reservedSeats = Integer.parseInt(seatsText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Number of seats must be a number");
            return;
        }

        String firstName = names[0];
        String lastName = names[1];

        Task<Void> bookingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {

                Client client = server.findClientByNames(firstName, lastName);

                if (client == null) {
                    client = new Client(firstName, lastName, email, phone);
                    client.setId(0);
                    server.saveClient(client);
                    client = server.findClientByNames(firstName, lastName);
                }

                Reservation reservation = new Reservation(client, selectedTrip, reservedSeats);
                Reservation saved = server.saveReservation(reservation);

                Platform.runLater(() -> {
                    if (saved != null) {
                        messageLabel.setText("Reservation saved");
                    } else {
                        messageLabel.setText("Reservation not saved");
                    }
                });

                return null;
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    messageLabel.setText("Error during reservation: " + getException().getMessage());
                });
                getException().printStackTrace();
            }
        };

        new Thread(bookingTask).start();
    }



    public void reservationAdded(Reservation reservation) throws ServiceException{

        Platform.runLater(() -> {
            for (int i = 0; i < modelList.size(); i++) {
                Trip t = modelList.get(i);
                if (t.getDestination().equals(reservation.getTrip().getDestination()) &&
                        t.getDate().equals(reservation.getTrip().getDate())) {

                    int newSeats = t.getNrSeats() - reservation.getNrReservedSeats();
                    Trip updatedTrip = new Trip(t.getDestination(), t.getDate(), newSeats);
                    modelList.set(i, updatedTrip);
                    break;
                }
            }
        });

    }

    @FXML
    public void onBtnLogOutClicked(ActionEvent actionEvent) throws IOException {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    void logout() {
        try {
            server.logout(admin);
        } catch (ServiceException e) {
            logger.error("Logout error {}", String.valueOf(e));
        }
    }
}


