package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ContaComSaldoException extends BancoException{

	public ContaComSaldoException() {
		super("Não é possivel remover uma conta que possui saldo. Faça o saque total do saldo e tente novamente.");
        this.httpStatus = HttpStatus.BAD_REQUEST;
	}
}
