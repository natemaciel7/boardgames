package com.locadora.boardgames.integration;

import com.locadora.boardgames.dtos.CustomerDTO;
import com.locadora.boardgames.models.Customer;
import com.locadora.boardgames.repositories.CustomerRepository;
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
 class CustomerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/customers";
    }

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomerAndGetById() {
        CustomerDTO dto = new CustomerDTO("João", "12345678901", "48999999999");
        ResponseEntity<Customer> create = restTemplate.postForEntity(getBaseUrl(), dto, Customer.class);
        assertEquals(HttpStatus.CREATED, create.getStatusCode());

        Long id = create.getBody().getId();
        ResponseEntity<Customer> get = restTemplate.getForEntity(getBaseUrl() + "/" + id, Customer.class);
        assertEquals("João", get.getBody().getName());
    }

    @Test
    void shouldNotCreateDuplicateCpf() {
        customerRepository.save(new Customer(null, "Maria", "12345678901", "48999999999"));
        CustomerDTO dto = new CustomerDTO("João", "12345678901", "48999999988");

        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), dto, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
