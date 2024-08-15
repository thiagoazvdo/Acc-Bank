package com.accenture.academico.Acc.Bank.exception.agencia;
import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.NegocioException;

public class AgenciaNaoEncontradaException extends NegocioException {
	
    public AgenciaNaoEncontradaException(Long id) {
    	super(String.format("Não existe uma agencia cadastrada com o id %d.", id));
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
