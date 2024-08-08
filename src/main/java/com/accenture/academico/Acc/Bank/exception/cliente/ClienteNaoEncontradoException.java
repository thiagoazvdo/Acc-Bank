package com.accenture.academico.Acc.Bank.exception.cliente;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ClienteNaoEncontradoException extends BancoException {

    public ClienteNaoEncontradoException(Long id) {
        super(String.format("Nao existe um cliente cadastrado com o id %d", id));
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
