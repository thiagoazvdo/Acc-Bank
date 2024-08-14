package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.NegocioException;

public class TransferenciaEntreContasIguaisException extends NegocioException{

	public TransferenciaEntreContasIguaisException() {
		super("Nao eh possivel fazer uma transferencia entre duas contas iguais");
        this.httpStatus = HttpStatus.BAD_REQUEST;
	}
}
