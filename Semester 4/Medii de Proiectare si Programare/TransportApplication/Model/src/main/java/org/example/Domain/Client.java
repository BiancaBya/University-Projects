package org.example.Domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table( name = "Clients" )
public class Client extends org.example.Domain.Entity<Integer> {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Client() {}

    public Client(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    @Id
    @GeneratedValue(strategy=IDENTITY)
    public Integer getId() {
        return super.getId();
    }
    public void setId(int id) {
        super.setId(id);
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

