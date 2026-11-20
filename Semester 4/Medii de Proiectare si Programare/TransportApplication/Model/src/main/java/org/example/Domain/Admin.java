package org.example.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table( name = "Admins" )
public class Admin extends org.example.Domain.Entity<Integer>{

    private String email;
    private String password;

    public Admin() {}

    @Id
    @GeneratedValue(strategy=IDENTITY)
    public Integer getId() {
        return super.getId();
    }
    public void setId(int id) {
        super.setId(id);
    }

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

