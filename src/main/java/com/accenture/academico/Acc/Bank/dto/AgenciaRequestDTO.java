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
public class AgenciaRequestDTO {

    @NotBlank(message = "Campo nome obrigatorio")
    private String nome;

    @NotBlank(message = "Campo endereco obrigatorio")
    private String endereco;

    @NotBlank(message = "Campo telefone obrigatorio")
    @Pattern(regexp = "\\d{11}", message = "Telefone deve ter exatamente 11 digitos numericos")
    private String telefone;

}
