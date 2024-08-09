package com.accenture.academico.Acc.Bank.dto;


import jakarta.persistence.Column;
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

    @Pattern(regexp = "\\d{11}", message = "Cpf deve ter exatamente 11 digitos numericos")
    @NotBlank(message = "Campo cpf obrigatorio")
    private String cpf;

    @NotBlank(message = "Campo telefone obrigatorio")
    private String telefone;

    @NotNull(message = "Campo id da agencia obrigatorio")
    @Column(name = "agencia_id", nullable = false)
    private Long idAgencia;

}
