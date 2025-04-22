package com.locadora.boardgames.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String image;

    @Min(1)
    private int stockTotal;

    @Min(1)
    private int pricePerDay;
}
