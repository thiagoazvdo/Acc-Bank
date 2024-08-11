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

    @NotBlank(message = "Campo nome obrigatorio")
    private String nome;

    @NotBlank(message = "Campo cpf obrigatorio")
    @Pattern(regexp = "\\d{11}", message = "Cpf deve ter exatamente 11 digitos numericos")
    private String cpf;

    @NotBlank(message = "Campo telefone obrigatorio")
    @Pattern(regexp = "\\d{11}", message = "Telefone deve ter exatamente 11 digitos numericos")
    private String telefone;

    @NotNull(message = "Campo idAgencia obrigatorio")
    private Long idAgencia;

}
