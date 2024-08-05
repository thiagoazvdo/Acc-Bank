package com.accenture.academico.Acc.Bank.dto;


import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Campo nome obrigatório")
    private String nome;

    @Pattern(regexp = "\\d{11}", message = "Cpf deve ter exatamente 11 digitos numericos")
    @NotBlank(message = "Campo cpf obrigatório!")
    private String cpf;

    @NotBlank(message = "Campo telefone obrigatório")
    private String telefone;

}
