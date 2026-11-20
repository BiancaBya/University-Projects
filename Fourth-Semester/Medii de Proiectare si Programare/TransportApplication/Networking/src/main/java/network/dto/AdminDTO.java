package network.dto;

import org.example.Domain.Entity;

public class AdminDTO extends Entity<Integer> {

    private String email;
    private String password;

    public AdminDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
