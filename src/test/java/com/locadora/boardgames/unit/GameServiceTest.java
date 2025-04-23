package com.locadora.boardgames.unit;

import com.locadora.boardgames.dtos.GameDTO;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.repositories.GameRepository;
import com.locadora.boardgames.services.GameService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;

    @BeforeEach
    void setup() {
        gameRepository = mock(GameRepository.class);
        gameService = new GameService(gameRepository);
    }

    @Test
    void shouldReturnAllGames() {
        when(gameRepository.findAll()).thenReturn(List.of(new Game()));
        List<Game> games = gameService.getAllGames();
        assertEquals(1, games.size());
    }

    @Test
    void shouldThrowWhenGameNameExists() {
        GameDTO dto = new GameDTO("Jogo", "", 2, 1000);
        when(gameRepository.findByName("Jogo")).thenReturn(Optional.of(new Game()));
        assertThrows(EntityExistsException.class, () -> gameService.createGame(dto));
    }

    @Test
    void shouldCreateGame() {
        GameDTO dto = new GameDTO("Jogo", "img", 2, 1000);
        when(gameRepository.findByName("Jogo")).thenReturn(Optional.empty());
        when(gameRepository.save(any())).thenReturn(new Game(1L, "Jogo", "img", 2, 1000));

        Game created = gameService.createGame(dto);

        assertNotNull(created);
        assertEquals("Jogo", created.getName());
    }
}
