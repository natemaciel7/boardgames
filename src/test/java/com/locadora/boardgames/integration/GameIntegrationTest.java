package com.locadora.boardgames.integration;

import com.locadora.boardgames.dtos.GameDTO;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.repositories.GameRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameRepository gameRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/games";
    }

    @BeforeEach
    void setup() {
        gameRepository.deleteAll();
    }

    @Test
    void shouldCreateGameAndList() {
        GameDTO dto = new GameDTO("Detetive", "img", 2, 1500);
        ResponseEntity<Game> response = restTemplate.postForEntity(getBaseUrl(), dto, Game.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<Game[]> list = restTemplate.getForEntity(getBaseUrl(), Game[].class);
        assertEquals(1, list.getBody().length);
    }

    @Test
    void shouldNotCreateDuplicateGame() {
        gameRepository.save(new Game(null, "Detetive", "img", 3, 1000));
        GameDTO dto = new GameDTO("Detetive", "img", 2, 1500);

        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), dto, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
