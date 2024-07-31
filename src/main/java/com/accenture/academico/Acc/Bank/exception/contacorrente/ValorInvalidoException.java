package com.accenture.academico.Acc.Bank.exception.contacorrente;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ValorInvalidoException extends BancoException{

	public ValorInvalidoException() {
		super("O valor dessa operação deve ser maior que zero!");
	}
}
