package com.accenture.academico.Acc.Bank.dto;

import com.accenture.academico.Acc.Bank.model.Agencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaCorrenteResponse {

    private Long id;

    private String numero;

    private BigDecimal saldo;

    private Agencia agencia;

}
