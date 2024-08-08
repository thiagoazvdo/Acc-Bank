package com.accenture.academico.Acc.Bank.exception.contacorrente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ContaCorrenteNaoEncontradaException extends BancoException{

	public ContaCorrenteNaoEncontradaException(Long idConta) {
		super(String.format("Nao existe uma conta corrente cadastrada com o id %d", idConta));
        this.httpStatus = HttpStatus.NOT_FOUND;
	}
	
	public ContaCorrenteNaoEncontradaException(String numeroConta) {
		super(String.format("Nao existe uma conta corrente cadastrada com o numero %s", numeroConta));
        this.httpStatus = HttpStatus.NOT_FOUND;
	}
}
