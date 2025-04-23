package com.locadora.boardgames.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RentalDTO(
        @NotNull(message = "ID do cliente é obrigatório")
        Long customerId,

        @NotNull(message = "ID do jogo é obrigatório")
        Long gameId,

        @NotNull(message = "Dias de aluguel obrigatórios")
        @Min(value = 1, message = "Deve alugar por pelo menos 1 dia")
        Integer daysRented
) {}
