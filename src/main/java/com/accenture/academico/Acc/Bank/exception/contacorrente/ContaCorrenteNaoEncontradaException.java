package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ContaCorrenteNaoEncontradaException extends BancoException{

	public ContaCorrenteNaoEncontradaException(Long id) {
		super(String.format("A conta corrente com o id %d não existe.", id));
        this.httpStatus = HttpStatus.NOT_FOUND;
	}
}
