package com.mobile.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User extends BaseEntity<Long> {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

}
