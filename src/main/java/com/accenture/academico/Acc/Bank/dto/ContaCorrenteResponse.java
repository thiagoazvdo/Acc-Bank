package com.accenture.academico.Acc.Bank.dto;

import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
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

    public ContaCorrenteResponse toEntity(ContaCorrente contaCorrente) {
        this.id = contaCorrente.getId();
        this.numero = contaCorrente.getNumero();
        this.saldo = contaCorrente.getSaldo();
        this.agencia = contaCorrente.getAgencia();
        return this;
    }
}
