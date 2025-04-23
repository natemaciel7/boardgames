package com.locadora.boardgames.repositories;

import com.locadora.boardgames.models.Rental;
import com.locadora.boardgames.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    long countByGameAndReturnDateIsNull(Game game);
}
