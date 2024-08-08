package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class TransferenciaEntreContasIguaisException extends BancoException{

	public TransferenciaEntreContasIguaisException() {
		super("Nao eh possivel fazer uma transferencia entre duas contas iguais");
        this.httpStatus = HttpStatus.BAD_REQUEST;
	}
}
