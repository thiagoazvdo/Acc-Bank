package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ValorInvalidoException extends BancoException{

	public ValorInvalidoException() {
		super("O valor dessa operação deve ser maior que zero!");
        this.httpStatus = HttpStatus.BAD_REQUEST;
	}
}
