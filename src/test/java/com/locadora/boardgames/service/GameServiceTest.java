package com.locadora.boardgames.service;

import com.locadora.boardgames.dto.GameRequestDTO;
import com.locadora.boardgames.dto.GameResponseDTO;
import com.locadora.boardgames.model.Game;
import com.locadora.boardgames.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameRepository repository;
    private GameService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(GameRepository.class);
        service = new GameService(repository);
    }

    @Test
    void testCreateGameComSucesso() {
        GameRequestDTO dto = new GameRequestDTO();
        dto.setName("Detetive");
        dto.setImage("http://imagem.com");
        dto.setStockTotal(3);
        dto.setPricePerDay(1500);

        when(repository.findByNameIgnoreCase("Detetive")).thenReturn(Optional.empty());

        Game game = new Game(1L, "Detetive", "http://imagem.com", 3, 1500);
        when(repository.save(any(Game.class))).thenReturn(game);

        GameResponseDTO response = service.create(dto);

        assertNotNull(response);
        assertEquals("Detetive", response.getName());
    }

    @Test
    void testCreateGameComNomeDuplicado() {
        GameRequestDTO dto = new GameRequestDTO();
        dto.setName("Catan");
        dto.setImage("http://catan.com");
        dto.setStockTotal(2);
        dto.setPricePerDay(2000);

        when(repository.findByNameIgnoreCase("Catan")).thenReturn(Optional.of(new Game()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(dto));
        assertEquals("Game already exists", ex.getMessage());
    }

    @Test
    void testFindAllGames() {
        Game game = new Game(1L, "Imagem & Ação", "http://imagem.com", 5, 1300);
        when(repository.findAll()).thenReturn(List.of(game));

        List<GameResponseDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Imagem & Ação", result.get(0).getName());
    }
}
