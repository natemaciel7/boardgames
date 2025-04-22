package com.locadora.boardgames.controller;

import com.locadora.boardgames.dto.RentalRequestDTO;
import com.locadora.boardgames.dto.RentalResponseDTO;
import com.locadora.boardgames.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody @Valid RentalRequestDTO dto) {
        try {
            RentalResponseDTO created = rentalService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long id) {
        try {
            rentalService.returnRental(id);
            return ResponseEntity.ok("Rental returned successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
