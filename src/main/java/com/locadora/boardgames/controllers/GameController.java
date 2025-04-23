package com.locadora.boardgames.controllers;

import com.locadora.boardgames.dtos.GameDTO;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.services.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody @Valid GameDTO dto) {
        return ResponseEntity.status(201).body(gameService.createGame(dto));
    }
}
