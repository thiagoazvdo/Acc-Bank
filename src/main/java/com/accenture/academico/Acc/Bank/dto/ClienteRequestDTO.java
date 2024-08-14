package com.accenture.academico.Acc.Bank.dto;


import com.accenture.academico.Acc.Bank.validation.ValidCPF;
import com.accenture.academico.Acc.Bank.validation.ValidEmail;
import com.accenture.academico.Acc.Bank.validation.ValidTelefone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @ValidCPF
    @NotBlank
    private String cpf;

    @ValidTelefone
    @NotBlank
    private String telefone;
    
    @ValidEmail
    @NotBlank
    private String email;

    @NotNull
    private Long idAgencia;

}
