package com.locadora.boardgames.controllers;

import com.locadora.boardgames.dtos.RentalDTO;
import com.locadora.boardgames.models.Rental;
import com.locadora.boardgames.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestBody @Valid RentalDTO dto) {
        return ResponseEntity.status(201).body(rentalService.createRental(dto));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.returnRental(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.ok().build();
    }
}
