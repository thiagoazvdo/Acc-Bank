package com.accenture.academico.Acc.Bank.dto;

import com.accenture.academico.Acc.Bank.validation.ValidTelefone;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgenciaRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @ValidTelefone
    @NotBlank
    private String telefone;

}
