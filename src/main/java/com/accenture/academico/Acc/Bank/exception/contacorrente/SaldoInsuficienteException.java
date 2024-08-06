package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class SaldoInsuficienteException extends BancoException{

	public SaldoInsuficienteException() {
		super("Saldo insuficiente para realizar essa operacao");
        this.httpStatus = HttpStatus.BAD_REQUEST;
	}
}
