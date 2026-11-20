package org.example.Repository.utils;

import org.example.Domain.Client;
import org.example.Domain.Trip;
import org.example.Repository.Hibernate.ClientRepoHibernate;
import org.example.Repository.Hibernate.TripRepoHibernate;

import java.time.LocalDateTime;

public class HibernateTestTrip {
    public static void main(String[] args) {
        System.out.println("Starting Trip main");
        TripRepoHibernate tripRepo = new TripRepoHibernate();

        Trip trip = new Trip("destination", LocalDateTime.now(), 18);

        System.out.println("Salvam tripul " + trip);
        tripRepo.save(trip);
//
//        Client client1 = repo.findOne(1).orElse(null);
//        if (client1!=null)
//            System.out.println("Am gasit clientul cu id-ul 1 " + client1);
//        else
//            System.out.println("NU am gasit clientul cu id-ul 1");
//
//        System.out.println("Stergem clientul cu id-ul 10");
//        repo.delete(11);
//
//        System.out.println("Actualizam  mesajul cu id-ul 5");
//        Client client2=repo.findOne(26).orElse(null);
//        assert client2 != null;
//        client2.setEmail("nou@nou.com");
//
//        repo.update(client2);


        System.out.println("Toate tripurile");
        for(Trip c : tripRepo.findAll()) {
            System.out.println(c);
            System.out.println("-------------------------");
        }
        System.out.println("-------------------------");

        HibernateUtils.closeSessionFactory();
    }
}
