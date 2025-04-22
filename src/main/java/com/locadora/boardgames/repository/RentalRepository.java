package com.locadora.boardgames.repository;

import com.locadora.boardgames.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    long countByGameIdAndReturnDateIsNull(Long gameId);
}
