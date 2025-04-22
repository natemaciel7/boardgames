package com.locadora.boardgames.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locadora.boardgames.dto.CustomerRequestDTO;
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
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomerComSucesso() throws Exception {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setName("Maria Teste");
        dto.setCpf(String.valueOf(System.currentTimeMillis()).substring(0, 11));
        dto.setPhone("11999999999");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCreateCustomerComCpfDuplicado() throws Exception {
        String cpfDuplicado = "12345678901";

        CustomerRequestDTO dto1 = new CustomerRequestDTO();
        dto1.setName("Cliente 1");
        dto1.setCpf(cpfDuplicado);
        dto1.setPhone("11999999999");

        CustomerRequestDTO dto2 = new CustomerRequestDTO();
        dto2.setName("Cliente 2");
        dto2.setCpf(cpfDuplicado);
        dto2.setPhone("11988888888");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isConflict())
                .andExpect(content().string("CPF already exists"));
    }
}
