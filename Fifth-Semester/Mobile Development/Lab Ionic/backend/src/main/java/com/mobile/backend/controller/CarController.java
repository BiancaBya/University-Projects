package com.mobile.backend.controller;

import com.mobile.backend.model.Car;
import com.mobile.backend.service.CarService;
import com.mobile.backend.service.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private CarService carService;
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<Car> saveCar(@RequestBody Car car) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.save(car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.ok("Car deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        return ResponseEntity.ok(carService.update(id, updatedCar));
    }

    @GetMapping
    public ResponseEntity<List<Car>> findAllCars() {
        List<Car> cars = carService.findAll();
        return cars.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> findCarById(@PathVariable Long id) {
        try {
            Car car = carService.findById(id);
            return ResponseEntity.ok(car);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping
//    public Page<Car> getCars(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false, name = "model") String model) {
//        return carService.findAllPaged(model, page, size);
//    }

    @PostMapping(path = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Car> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Car car = carService.findById(id);
        String publicUrl = fileStorageService.store(file, "car_" + id);

        fileStorageService.deleteIfExists(car.getPhotoUrl());

        car.setPhotoUrl(publicUrl);
        Car saved = carService.save(car);

        return ResponseEntity.ok(saved);
    }

}


