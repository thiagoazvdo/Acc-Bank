package com.accenture.academico.Acc.Bank.exception.cliente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.NegocioException;

public class ClienteNaoEncontradoException extends NegocioException {

    public ClienteNaoEncontradoException(Long id) {
        super(String.format("Não existe um cliente cadastrado com o id %d.", id));
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
