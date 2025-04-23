package com.locadora.boardgames.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GameDTO(
        @NotBlank(message = "Nome não pode ser vazio")
        String name,

        String image,

        @NotNull(message = "Estoque obrigatório")
        @Min(value = 1, message = "Estoque deve ser maior que 0")
        Integer stockTotal,

        @NotNull(message = "Preço obrigatório")
        @Min(value = 1, message = "Preço deve ser maior que 0")
        Integer pricePerDay
) {}
