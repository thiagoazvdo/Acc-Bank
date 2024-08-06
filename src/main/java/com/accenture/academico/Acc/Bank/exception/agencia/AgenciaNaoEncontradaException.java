package com.accenture.academico.Acc.Bank.exception.agencia;
import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class AgenciaNaoEncontradaException extends BancoException {
	
    public AgenciaNaoEncontradaException(Long id) {
    	super(String.format("Nao existe uma agencia cadastrada com o id %d", id));
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
