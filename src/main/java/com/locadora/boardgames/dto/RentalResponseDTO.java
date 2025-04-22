package com.locadora.boardgames.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RentalResponseDTO {
    private Long id;
    private Long customerId;
    private Long gameId;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private int originalPrice;
    private Integer delayFee;
}
