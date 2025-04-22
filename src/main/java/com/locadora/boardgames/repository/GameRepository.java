package com.locadora.boardgames.repository;

import com.locadora.boardgames.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByNameIgnoreCase(String name);
}
