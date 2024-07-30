package com.accenture.academico.Acc.Bank.exception.contacorrente;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class SaldoInsuficienteException extends BancoException{

	public SaldoInsuficienteException() {
		super("Saldo insuficiente para realizar essa operação!");
	}
}
