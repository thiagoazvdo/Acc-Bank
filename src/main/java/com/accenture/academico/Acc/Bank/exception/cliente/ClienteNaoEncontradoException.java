package com.accenture.academico.Acc.Bank.exception.cliente;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ClienteNaoEncontradoException extends BancoException {

    public ClienteNaoEncontradoException() {
        super("Não existe um cliente cadastrado com o CPF informado");
    }
}
