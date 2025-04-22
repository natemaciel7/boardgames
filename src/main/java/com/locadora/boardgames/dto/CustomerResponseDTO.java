package com.locadora.boardgames.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String cpf;
    private String phone;
}
