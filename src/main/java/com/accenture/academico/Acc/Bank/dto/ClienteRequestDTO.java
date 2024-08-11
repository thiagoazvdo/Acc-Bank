package com.accenture.academico.Acc.Bank.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "Cpf deve ter exatamente 11 digitos numericos")
    private String cpf;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "Telefone deve ter exatamente 11 digitos numericos")
    private String telefone;

    @NotNull
    private Long idAgencia;

}
