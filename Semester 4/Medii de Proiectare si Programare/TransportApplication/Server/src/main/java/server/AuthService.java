package server;

import org.example.Domain.Admin;
import org.example.Repository.AdminRepoInterface;
import org.example.Repository.Hibernate.AdminRepoHibernate;

import java.util.Optional;

public class AuthService {

    private final AdminRepoInterface adminRepo;

    public AuthService(){
        adminRepo = new AdminRepoHibernate();
    }

    public Admin login(String email, String password) {
        try {
            Admin admin = null;
            for (Admin a : adminRepo.findAll()) {
                if (a.getEmail().equals(email) && a.getPassword().equals(password)) {
                    admin = a;
                }
            }
            if (admin == null)
                throw new IllegalArgumentException("Invalid credentials");
            return admin;
        } catch (Exception e) {
            throw e;
        }
    }

}


