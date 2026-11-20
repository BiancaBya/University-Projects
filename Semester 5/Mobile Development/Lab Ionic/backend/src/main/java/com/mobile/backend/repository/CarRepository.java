package com.mobile.backend.repository;

import com.mobile.backend.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByOwner_Id(Long ownerId);
    Page<Car> findByOwner_IdOrderByIdAsc(Long ownerId, Pageable pageable);
    Page<Car> findByOwner_IdAndModelContainingIgnoreCaseOrderByIdAsc(
            Long ownerId, String model, Pageable pageable
    );
}


