package com.mobile.backend.service;

import com.mobile.backend.events.CarEvent;
import com.mobile.backend.model.Car;
import com.mobile.backend.model.User;
import com.mobile.backend.repository.CarRepository;
import com.mobile.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    public Car save(Car car) {
        car.setOwner(currentUser());
        var saved = carRepository.save(car);
        messagingTemplate.convertAndSendToUser(saved.getOwner().getUsername(), "/queue/notifications",
                CarEvent.builder().type(CarEvent.Type.INSERT).car(saved).build());
        return saved;
    }

    public void delete(Long id) {
        var car = carRepository.findById(id).orElseThrow();
        requireOwner(car);
        carRepository.delete(car);
        messagingTemplate.convertAndSendToUser(car.getOwner().getUsername(), "/queue/notifications",
                CarEvent.builder().type(CarEvent.Type.DELETE).car(car).build());
    }

    public Car update(Long id, Car incoming) {
        var existing = carRepository.findById(id).orElseThrow();
        requireOwner(existing);
        existing.setModel(incoming.getModel());
        existing.setManufacturingDate(incoming.getManufacturingDate());
        existing.setOwned(incoming.isOwned());
        var updated = carRepository.save(existing);
        messagingTemplate.convertAndSendToUser(updated.getOwner().getUsername(), "/queue/notifications",
                CarEvent.builder().type(CarEvent.Type.UPDATE).car(updated).build());
        return updated;
    }

    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow();
    }

    public List<Car> findAll() {
        return carRepository.findAllByOwner_Id(currentUser().getId());
    }

    private void requireOwner(Car car) {
        if (!car.getOwner().getId().equals(currentUser().getId()))
            throw new AccessDeniedException("not your car");
    }

    public Page<Car> findAllPaged(String model, int page, int size) {
        Long ownerId = currentUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        if (model != null && !model.isBlank()) {
            return carRepository.findByOwner_IdAndModelContainingIgnoreCaseOrderByIdAsc(ownerId, model, pageable);
        }
        return carRepository.findByOwner_IdOrderByIdAsc(ownerId, pageable);
    }

}


