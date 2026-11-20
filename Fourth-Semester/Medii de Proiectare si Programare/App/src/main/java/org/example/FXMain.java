package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import org.example.Controllers.LoginController;
import org.example.Repository.RepositoryDB.AdminRepoDB;
import org.example.Repository.RepositoryDB.ClientRepoDB;
import org.example.Repository.RepositoryDB.ReservationRepoDB;
import org.example.Repository.RepositoryDB.TripRepoDB;
import org.example.Service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FXMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/login.fxml"));
        AnchorPane layout = fxmlLoader.load();
        stage.setScene(new Scene(layout));

        Service service = getService();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);

        stage.show();
    }

    static Service getService(){

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        AdminRepoDB repoAdmin = new AdminRepoDB(props);
        ClientRepoDB repoClient = new ClientRepoDB(props);
        TripRepoDB repoTrip = new TripRepoDB(props);
        ReservationRepoDB reservationRepoDB = new ReservationRepoDB(props, repoClient, repoTrip);
        return new Service(repoAdmin, repoClient, repoTrip, reservationRepoDB);
    }


    public static void main(String[] args) {
        launch(args);
    }
}


