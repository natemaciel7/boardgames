package com.locadora.boardgames.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locadora.boardgames.dto.GameRequestDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateGameComSucesso() throws Exception {
        GameRequestDTO game = new GameRequestDTO("Catan" + System.currentTimeMillis(), "http://imagem.com/catan.jpg", 2, 1200);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllGames() throws Exception {
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
void testCreateGameComNomeDuplicado() throws Exception {
    GameRequestDTO dto1 = new GameRequestDTO();
    dto1.setName("Catan");
    dto1.setImage("http://imagem.com/catan.jpg");
    dto1.setStockTotal(2);
    dto1.setPricePerDay(1200);

    GameRequestDTO dto2 = new GameRequestDTO();
    dto2.setName("Catan");
    dto2.setImage("http://imagem.com/catan-duplicado.jpg");
    dto2.setStockTotal(3);
    dto2.setPricePerDay(1500);

    mockMvc.perform(post("/games")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto1)))
            .andExpect(status().isCreated());

    mockMvc.perform(post("/games")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto2)))
            .andExpect(status().isConflict())
            .andExpect(content().string("Game already exists"));
}
}
