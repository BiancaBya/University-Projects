package org.example.Repository.utils;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Repository.Hibernate.AdminRepoHibernate;
import org.example.Repository.Hibernate.ClientRepoHibernate;
import org.example.Repository.RepositoryDB.AdminRepoDB;
import org.example.Repository.RepositoryDB.ReservationRepoDB;
import org.example.Repository.RepositoryDB.TripRepoDB;

public class HibernateTestAdmin {
    public static void main(String[] args) {
        System.out.println("Starting Admin main");
        AdminRepoHibernate repo = new AdminRepoHibernate();

        Admin admin = new Admin("bibi@gmail.com", "parola");

        System.out.println("Salvam adminul " + admin);
        repo.save(admin);

        Admin admin1 = repo.findOne(1).orElse(null);
        if (admin1!=null)
            System.out.println("Am gasit adminul cu id-ul 1 " + admin1);
        else
            System.out.println("NU am gasit adminul cu id-ul 1");

        System.out.println("Stergem adminul cu id-ul 6");
        repo.delete(10);

        System.out.println("Actualizam  adminul cu id-ul 5");
        Admin admin2=repo.findOne(7).orElse(null);
        assert admin2 != null;
        admin2.setEmail("nou@nou.com");

        repo.update(admin2);


        System.out.println("Toti adminii");
        for(Admin a : repo.findAll()) {
            System.out.println(a);
            System.out.println("-------------------------");
        }
        System.out.println("-------------------------");

        HibernateUtils.closeSessionFactory();

    }
}
