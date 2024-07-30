package com.accenture.academico.Acc.Bank.expection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNaoEncontradoException extends NegocioException {

    public ClienteNaoEncontradoException() {
        super("Não existe um cliente cadastrado com o CPF informado");
    }
}
