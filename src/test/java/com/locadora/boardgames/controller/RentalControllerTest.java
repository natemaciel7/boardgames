package com.locadora.boardgames.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locadora.boardgames.dto.RentalRequestDTO;
import com.locadora.boardgames.model.Customer;
import com.locadora.boardgames.model.Game;
import com.locadora.boardgames.repository.CustomerRepository;
import com.locadora.boardgames.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Long gameId;
    private Long customerId;

    @BeforeEach
    void setup() {
        Game game = new Game(null, "Jogo Teste " + System.currentTimeMillis(), "img", 2, 1000);
        gameId = gameRepository.save(game).getId();

        String cpfUnico = String.valueOf(System.currentTimeMillis()).substring(0, 11);
        Customer customer = new Customer(null, "Cliente Teste", cpfUnico, "11999999999");        
        customerId = customerRepository.save(customer).getId();
    }

    @Test
    void testCreateRental() throws Exception {
        RentalRequestDTO dto = new RentalRequestDTO();
        dto.setCustomerId(customerId);
        dto.setGameId(gameId);
        dto.setDaysRented(3);

        mockMvc.perform(post("/rentals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }
}
