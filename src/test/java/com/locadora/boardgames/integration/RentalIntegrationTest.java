package com.locadora.boardgames.integration;

import com.locadora.boardgames.dtos.RentalDTO;
import com.locadora.boardgames.models.Customer;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.models.Rental;
import com.locadora.boardgames.repositories.CustomerRepository;
import com.locadora.boardgames.repositories.GameRepository;
import com.locadora.boardgames.repositories.RentalRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class RentalIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RentalRepository rentalRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/rentals";
    }

    private Customer createCustomer() {
        return customerRepository.save(new Customer(null, "João", "12345678900", "48999999999"));
    }

    private Game createGame() {
        return gameRepository.save(new Game(null, "Detetive", "img", 2, 1500));
    }

    @BeforeEach
    void setup() {
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    void shouldCreateRentalAndReturnIt() {
        Customer customer = createCustomer();
        Game game = createGame();

        RentalDTO dto = new RentalDTO(customer.getId(), game.getId(), 3);
        ResponseEntity<Rental> createResponse = restTemplate.postForEntity(getBaseUrl(), dto, Rental.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long rentalId = Objects.requireNonNull(createResponse.getBody()).getId();

        ResponseEntity<Rental> returnResponse = restTemplate.postForEntity(getBaseUrl() + "/" + rentalId + "/return", null, Rental.class);
        assertEquals(HttpStatus.OK, returnResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(returnResponse.getBody()).getReturnDate());
    }

@Test
void shouldNotDeleteRentalIfNotReturned() {
    Customer customer = createCustomer();
    Game game = createGame();

    Rental rental = new Rental();
    rental.setCustomer(customer);
    rental.setGame(game);
    rental.setRentDate(LocalDate.now());
    rental.setDaysRented(3);
    rental.setOriginalPrice(4500);
    rental.setDelayFee(0);
    rental.setReturnDate(null);

    rental = rentalRepository.saveAndFlush(rental);

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<Void> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        getBaseUrl() + "/" + rental.getId(),
        HttpMethod.DELETE,
        request,
        String.class
    );

    System.out.println("STATUS: " + response.getStatusCode());
    System.out.println("BODY: " + response.getBody());

    assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
}
    @Test
    void shouldDeleteRentalAfterReturn() {
        Customer customer = createCustomer();
        Game game = createGame();

        Rental rental = new Rental(null, customer, game, java.time.LocalDate.now().minusDays(4), 3, java.time.LocalDate.now(), 4500, 0);
        rental = rentalRepository.save(rental);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + rental.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
}
