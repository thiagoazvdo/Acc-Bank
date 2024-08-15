package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.NegocioException;

public class TransferenciaEntreContasIguaisException extends NegocioException{

	public TransferenciaEntreContasIguaisException() {
		super("Não é possível fazer uma transferência entre duas contas iguais.");
        this.httpStatus = HttpStatus.BAD_REQUEST;
	}
}
