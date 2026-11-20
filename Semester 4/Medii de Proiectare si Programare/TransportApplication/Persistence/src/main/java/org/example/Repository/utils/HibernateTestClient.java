package org.example.Repository.utils;

import org.example.Domain.Client;
import org.example.Repository.Hibernate.ClientRepoHibernate;

public class HibernateTestClient {
    public static void main(String[] args) {
        System.out.println("Starting Client main");
        ClientRepoHibernate repo = new ClientRepoHibernate();

        Client client = new Client("clienttt", "nouttt", "nou", "nou");

        System.out.println("Salvam clientul " + client);
        repo.save(client);

        Client client1 = repo.findOne(1).orElse(null);
        if (client1!=null)
            System.out.println("Am gasit clientul cu id-ul 1 " + client1);
        else
            System.out.println("NU am gasit clientul cu id-ul 1");

        System.out.println("Stergem clientul cu id-ul 10");
        repo.delete(11);

        System.out.println("Actualizam  mesajul cu id-ul 5");
        Client client2=repo.findOne(26).orElse(null);
        assert client2 != null;
        client2.setEmail("nou@nou.com");

        repo.update(client2);


        System.out.println("Toti clientii");
        for(Client c : repo.findAll()) {
            System.out.println(c);
            System.out.println("-------------------------");
        }
        System.out.println("-------------------------");

        HibernateUtils.closeSessionFactory();
    }
}
