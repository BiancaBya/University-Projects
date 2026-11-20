package org.example;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Domain.Reservation;
import org.example.Domain.Trip;
import org.example.Repository.RepositoryDB.AdminRepoDB;
import org.example.Repository.RepositoryDB.ClientRepoDB;
import org.example.Repository.RepositoryDB.ReservationRepoDB;
import org.example.Repository.RepositoryDB.TripRepoDB;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
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

        for (Admin admin : repoAdmin.findAll()){
            System.out.println(admin);
        }
        for (Client client : repoClient.findAll()){
            System.out.println(client);
        }
        for (Trip trip : repoTrip.findAll()){
            System.out.println(trip);
        }
        for (Reservation reservation : reservationRepoDB.findAll()){
            System.out.println(reservation);
        }

    }
}



