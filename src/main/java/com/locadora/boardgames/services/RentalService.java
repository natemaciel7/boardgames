package com.locadora.boardgames.services;

import com.locadora.boardgames.dtos.RentalDTO;
import com.locadora.boardgames.models.Customer;
import com.locadora.boardgames.models.Game;
import com.locadora.boardgames.models.Rental;
import com.locadora.boardgames.repositories.CustomerRepository;
import com.locadora.boardgames.repositories.GameRepository;
import com.locadora.boardgames.repositories.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final GameRepository gameRepository;

    public RentalService(RentalRepository rentalRepository, CustomerRepository customerRepository, GameRepository gameRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.gameRepository = gameRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental createRental(RentalDTO dto) {
        if (dto.daysRented() <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Dias inválidos");
        }

        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cliente não encontrado"));

        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Jogo não encontrado"));

        long rentedCount = rentalRepository.countByGameAndReturnDateIsNull(game);
        if (rentedCount >= game.getStockTotal()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Estoque esgotado");
        }

        Rental rental = new Rental(null, customer, game, LocalDate.now(), dto.daysRented(), null,
                dto.daysRented() * game.getPricePerDay(), 0);

        return rentalRepository.save(rental);
    }

    public Rental returnRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Aluguel não encontrado"));

        if (rental.getReturnDate() != null) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Aluguel já finalizado");
        }

        LocalDate today = LocalDate.now();
        long atraso = ChronoUnit.DAYS.between(rental.getRentDate().plusDays(rental.getDaysRented()), today);
        int delayFee = atraso > 0 ? (int) (atraso * rental.getGame().getPricePerDay()) : 0;

        rental.setReturnDate(today);
        rental.setDelayFee(delayFee);

        return rentalRepository.save(rental);
    }

    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Aluguel não encontrado"));

        if (rental.getReturnDate() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Aluguel ainda não devolvido");
        }

        rentalRepository.delete(rental);
    }
}
