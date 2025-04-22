package com.locadora.boardgames.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestDTO {
    @NotNull
    private Long customerId;

    @NotNull
    private Long gameId;

    @Min(1)
    private int daysRented;
}
