package com.example.examen;

import Domain.Order;
import Domain.Status;
import Service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class HelloController {

    private Service service;
    private Stage stage;

    @FXML
    private Label mesaj;

    @FXML
    private TextField address;

    @FXML
    private TextField destination;

    @FXML
    private TextField client;

    @FXML
    private Button addBtn;


    public void onBtnAdd(){

        if(address.getText().isEmpty() || destination.getText().isEmpty() || client.getText().isEmpty()){
            mesaj.setText("Completati datele");
        }
        else {
            String adress = address.getText();
            String dest = destination.getText();
            String clientName = client.getText();

            service.addOrder(adress, dest, clientName);

            mesaj.setText("Cursa Adaugata");
        }

    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
