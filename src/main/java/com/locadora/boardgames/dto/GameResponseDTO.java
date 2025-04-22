package com.locadora.boardgames.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResponseDTO {
    private Long id;
    private String name;
    private String image;
    private int stockTotal;
    private int pricePerDay;
}
