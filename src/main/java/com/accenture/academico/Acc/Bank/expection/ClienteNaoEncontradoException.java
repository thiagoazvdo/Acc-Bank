package com.accenture.academico.Acc.Bank.expection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ClienteNaoEncontradoException extends BancoException {

    public ClienteNaoEncontradoException() {
        super("Não existe um cliente cadastrado com o CPF informado");
    }
}
