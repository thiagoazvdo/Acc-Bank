package com.accenture.academico.Acc.Bank.dto;

import java.math.BigDecimal;

import com.accenture.academico.Acc.Bank.model.Agencia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContaCorrenteResponseDTO {

    private Long id;

    private String numero;

    private BigDecimal saldo;

    private Agencia agencia;

}
