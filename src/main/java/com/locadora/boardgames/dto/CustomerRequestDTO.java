package com.locadora.boardgames.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 11, max = 11)
    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    private String phone;
}
