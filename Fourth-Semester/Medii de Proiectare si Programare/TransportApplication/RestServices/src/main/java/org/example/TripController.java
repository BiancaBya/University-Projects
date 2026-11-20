package org.example;

import org.example.Domain.Trip;
import org.example.Repository.Hibernate.TripRepoHibernate;
import org.example.Repository.RepositoryDB.TripRepoDB;
import org.example.Repository.TripRepoInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripRepoInterface tripRepo;

    public TripController() {
        this.tripRepo = new TripRepoHibernate();
    }

    @PostMapping
    public ResponseEntity<Integer> addTrip(@RequestBody Trip trip) {

        Optional<Trip> savedTrip = tripRepo.save(trip);

        if (savedTrip.isPresent()) {
            System.out.println("Saved Trip: " + savedTrip.get());
            return new ResponseEntity<>(savedTrip.get().getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    @GetMapping
    public ResponseEntity<Iterable<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Integer id) {
        Optional<Trip> trip = tripRepo.findOne(id);
        return trip.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @RequestBody Trip trip) {
        Optional<Trip> updatedTrip = tripRepo.update(trip);
        return updatedTrip.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Integer id) {
        Optional<Trip> deletedTrip = tripRepo.delete(id);
        if (deletedTrip.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


