package com.locadora.boardgames.service;

import com.locadora.boardgames.dto.RentalRequestDTO;
import com.locadora.boardgames.dto.RentalResponseDTO;
import com.locadora.boardgames.model.Customer;
import com.locadora.boardgames.model.Game;
import com.locadora.boardgames.model.Rental;
import com.locadora.boardgames.repository.CustomerRepository;
import com.locadora.boardgames.repository.GameRepository;
import com.locadora.boardgames.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<RentalResponseDTO> findAll() {
        return rentalRepository.findAll().stream()
                .map(r -> new RentalResponseDTO(
                        r.getId(), r.getCustomer().getId(), r.getGame().getId(), r.getRentDate(),
                        r.getDaysRented(), r.getReturnDate(), r.getOriginalPrice(), r.getDelayFee()))
                .collect(Collectors.toList());
    }

    public RentalResponseDTO create(RentalRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        long rentedCount = rentalRepository.countByGameIdAndReturnDateIsNull(game.getId());
        if (rentedCount >= game.getStockTotal()) {
            throw new RuntimeException("Game out of stock");
        }

        int originalPrice = game.getPricePerDay() * dto.getDaysRented();

        Rental rental = new Rental(null, customer, game, LocalDate.now(), dto.getDaysRented(), null, originalPrice, null);
        Rental saved = rentalRepository.save(rental);

        return new RentalResponseDTO(saved.getId(), customer.getId(), game.getId(), saved.getRentDate(),
                saved.getDaysRented(), saved.getReturnDate(), saved.getOriginalPrice(), saved.getDelayFee());
    }

    public void returnRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new RuntimeException("Rental already returned");
        }

        rental.setReturnDate(LocalDate.now());

        long delayDays = Math.max(0, LocalDate.now().toEpochDay() - rental.getRentDate().plusDays(rental.getDaysRented()).toEpochDay());
        rental.setDelayFee((int) (delayDays * rental.getGame().getPricePerDay()));

        rentalRepository.save(rental);
    }
}
