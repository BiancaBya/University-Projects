package com.example.examen;

import Domain.Driver;
import Domain.Order;
import Domain.Status;
import Service.Service;
import Utils.Events.ChangeEventType;
import Utils.Events.EntityChangeEvent;
import Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class DriverController implements Observer<EntityChangeEvent> {

    private Service service;
    private Driver driver;
    private Stage stage;

    private Order selectedOrder;

    ObservableList<Order> model = FXCollections.observableArrayList();

    @FXML
    ListView<Order> orderList;

    @FXML
    private Label mesaj;

    @FXML
    private Label numeSofer;

    @FXML
    private Button finishedBtn;

    @FXML
    private Button acceptBtn;


    private void initModel(){

        List<Order> comenzi = service.getAllDriversOrders(driver.getId());
        model.setAll(comenzi);
        orderList.setItems(model);

    }



    @FXML
    public void initialize() {
        orderList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Order selectedItem = orderList.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    selectedOrder = service.getOrder(selectedItem.getId());
                }
            }
        });
    }


    public void onBtnAccept(){

        Driver driverBun = service.getBestDriver();
        if (Objects.equals(driverBun.getId(), driver.getId())) {
            service.acceptOrder(driver.getId());
            initModel();
        }

    }


    public void onBtnFinshed(){

        if (selectedOrder != null) {
            selectedOrder.setStatus(Status.FINISHED);
            selectedOrder.setEndDate(LocalDateTime.now());
            service.modifyOrder(selectedOrder);
            initModel();
        }

    }



    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        numeSofer.setText(driver.getName());
        initModel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {

        if (entityChangeEvent.getChangeType() == ChangeEventType.ADD) {
            Driver soferBun = service.getBestDriver();
            if (soferBun != null) {
                if (Objects.equals(soferBun.getId(), driver.getId())) {
                    mesaj.setText("New order " + entityChangeEvent.getAffectedOrder().getAddress() + " -> " +
                                                entityChangeEvent.getAffectedOrder().getDestination());
                }
            }
        }
    }
}
