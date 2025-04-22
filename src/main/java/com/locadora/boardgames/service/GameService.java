package com.locadora.boardgames.service;

import com.locadora.boardgames.dto.GameRequestDTO;
import com.locadora.boardgames.dto.GameResponseDTO;
import com.locadora.boardgames.model.Game;
import com.locadora.boardgames.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public List<GameResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(g -> new GameResponseDTO(g.getId(), g.getName(), g.getImage(), g.getStockTotal(), g.getPricePerDay()))
                .collect(Collectors.toList());
    }

    public Optional<GameResponseDTO> findById(Long id) {
        return repository.findById(id)
                .map(g -> new GameResponseDTO(g.getId(), g.getName(), g.getImage(), g.getStockTotal(), g.getPricePerDay()));
    }

    public GameResponseDTO create(GameRequestDTO dto) {
        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Game already exists");
        }

        Game game = new Game(null, dto.getName(), dto.getImage(), dto.getStockTotal(), dto.getPricePerDay());
        Game saved = repository.save(game);
        return new GameResponseDTO(saved.getId(), saved.getName(), saved.getImage(), saved.getStockTotal(), saved.getPricePerDay());
    }
}
