package com.accenture.academico.Acc.Bank.dto;

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

    @NotBlank(message = "Campo nome obrigatório!")
    private String nome;

    @NotBlank(message = "Campo endereco obrigatório!")
    private String endereco;

    @NotBlank(message = "Campo telefone obrigatório!")
    private String telefone;

}
