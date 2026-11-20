package com.mobile.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Car extends BaseEntity<Long> {

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private LocalDate manufacturingDate;

    @Column(nullable = false)
    private boolean owned;

    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Column
    private String photoUrl;
}


