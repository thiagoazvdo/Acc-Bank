package com.accenture.academico.Acc.Bank.exception.contacorrente;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ContaCorrenteNaoExisteException extends BancoException{

	public ContaCorrenteNaoExisteException(Long id) {
		super(String.format("A conta corrente com o id %d não existe.", id));
	}
}
