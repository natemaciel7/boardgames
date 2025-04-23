package com.locadora.boardgames.unit;

import com.locadora.boardgames.dtos.RentalDTO;
import com.locadora.boardgames.models.Customer;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.models.Rental;
import com.locadora.boardgames.repositories.CustomerRepository;
import com.locadora.boardgames.repositories.GameRepository;
import com.locadora.boardgames.repositories.RentalRepository;
import com.locadora.boardgames.services.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    private RentalService rentalService;
    private RentalRepository rentalRepository;
    private CustomerRepository customerRepository;
    private GameRepository gameRepository;

    @BeforeEach
    void setup() {
        rentalRepository = mock(RentalRepository.class);
        customerRepository = mock(CustomerRepository.class);
        gameRepository = mock(GameRepository.class);
        rentalService = new RentalService(rentalRepository, customerRepository, gameRepository);
    }

    @Test
    void shouldCreateRentalSuccessfully() {
        RentalDTO dto = new RentalDTO(1L, 1L, 3);
        Customer customer = new Customer(1L, "João", "12345678900", "48999999999");
        Game game = new Game(1L, "Detetive", "img", 2, 1500);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(rentalRepository.countByGameAndReturnDateIsNull(game)).thenReturn(0L);
        when(rentalRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Rental rental = rentalService.createRental(dto);

        assertEquals(4500, rental.getOriginalPrice());
        assertEquals(LocalDate.now(), rental.getRentDate());
        assertNull(rental.getReturnDate());
    }

    @Test
    void shouldThrowIfRentalAlreadyFinalized() {
        Rental rental = new Rental(1L, null, null, LocalDate.now(), 3, LocalDate.now(), 4500, 0);
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        assertThrows(ResponseStatusException.class, () -> rentalService.returnRental(1L));
    }

    @Test
    void shouldCalculateDelayFeeWhenReturningLate() {
        Customer customer = new Customer(1L, "João", "12345678900", "48999999999");
        Game game = new Game(1L, "Detetive", "img", 2, 1500);
        Rental rental = new Rental(1L, customer, game, LocalDate.now().minusDays(5), 3, null, 4500, 0);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Rental returned = rentalService.returnRental(1L);

        assertNotNull(returned.getReturnDate());
        assertEquals(3000, returned.getDelayFee()); // 2 dias de atraso * 1500
    }

    @Test
    void shouldThrowWhenDeletingRentalNotFinalized() {
        Rental rental = new Rental(1L, null, null, LocalDate.now(), 3, null, 4500, 0);
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        assertThrows(ResponseStatusException.class, () -> rentalService.deleteRental(1L));
    }
}
