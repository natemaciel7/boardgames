package com.locadora.boardgames.services;

import com.locadora.boardgames.dtos.GameDTO;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.repositories.GameRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game createGame(GameDTO dto) {
        gameRepository.findByName(dto.name()).ifPresent(g -> {
            throw new EntityExistsException("Jogo já existe");
        });

        Game game = new Game(null, dto.name(), dto.image(), dto.stockTotal(), dto.pricePerDay());
        return gameRepository.save(game);
    }
}
