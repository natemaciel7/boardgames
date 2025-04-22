package com.locadora.boardgames.service;

import com.locadora.boardgames.dto.RentalRequestDTO;
import com.locadora.boardgames.dto.RentalResponseDTO;
import com.locadora.boardgames.model.Customer;
import com.locadora.boardgames.model.Game;
import com.locadora.boardgames.model.Rental;
import com.locadora.boardgames.repository.CustomerRepository;
import com.locadora.boardgames.repository.GameRepository;
import com.locadora.boardgames.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    private RentalRepository rentalRepository;
    private GameRepository gameRepository;
    private CustomerRepository customerRepository;
    private RentalService service;

    @BeforeEach
    void setup() {
        rentalRepository = Mockito.mock(RentalRepository.class);
        gameRepository = Mockito.mock(GameRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        service = new RentalService(rentalRepository, gameRepository, customerRepository);
    }

    @Test
    void testCreateRentalComSucesso() {
        RentalRequestDTO dto = new RentalRequestDTO();
        dto.setCustomerId(1L);
        dto.setGameId(1L);
        dto.setDaysRented(2);

        Game game = new Game(1L, "War", "http://war.com", 3, 1000);
        Customer customer = new Customer(1L, "Luiza", "12345678901", "11999999999");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.countByGameIdAndReturnDateIsNull(1L)).thenReturn(0);

        Rental rental = new Rental(1L, LocalDate.now(), null, 2, 2000, null, game, customer);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        RentalResponseDTO response = service.create(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("War", response.getGame().getName());
        assertEquals("Luiza", response.getCustomer().getName());
    }
}
