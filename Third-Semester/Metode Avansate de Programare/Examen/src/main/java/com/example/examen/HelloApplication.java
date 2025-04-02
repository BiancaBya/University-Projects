package com.example.examen;

import Domain.Driver;
import Repository.Repository;
import Repository.DriverDBRepository;
import Repository.OrderDBRepository;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class HelloApplication extends Application {


    private Service service;

    @Override
    public void start(Stage stage) throws IOException {

        try {
            String url = "jdbc:postgresql://localhost:5432/Examen";
            String username = "postgres";
            String password = "parola";

            Repository<Integer, Driver> repoDriver = new DriverDBRepository(url, username, password);
            OrderDBRepository repoOrder = new OrderDBRepository(url, username, password);

            service = new Service(repoDriver, repoOrder);


            initView(stage);
            stage.setTitle("Dispecerat");
            stage.setWidth(800);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView(Stage primaryStage) throws IOException{

        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("hello-view.fxml"));
        AnchorPane Layout = Loader.load();
        primaryStage.setScene(new Scene(Layout));

        HelloController helloController = Loader.getController();
        helloController.setService(service);
        helloController.setStage(primaryStage);
        helloController.setStage(primaryStage);

        List<Driver> drivers = service.getAllDrivers();
        int index = 1;
        for (Driver driver : drivers) {

            FXMLLoader driverLoader = new FXMLLoader();
            driverLoader.setLocation(getClass().getResource("driver-view.fxml"));
            AnchorPane driverLayout = driverLoader.load();

            Stage driverStage = new Stage();
            driverStage.setTitle("Driver " + index);
            driverStage.setScene(new Scene(driverLayout));

            DriverController driverController = driverLoader.getController();
            driverController.setService(service);
            driverController.setDriver(driver);
            driverController.setStage(primaryStage);

            driverStage.show();

            index++;

        }

    }


    public static void main(String[] args) {
        launch();
    }
}

