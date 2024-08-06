package com.accenture.academico.Acc.Bank.exception.cliente;

import com.accenture.academico.Acc.Bank.exception.BancoException;
import org.springframework.http.HttpStatus;

public class ClienteJaCadastradoException extends BancoException {

    public ClienteJaCadastradoException(String cpf) {
        super(String.format("Ja existe um cliente cadastrado com o cpf %s", cpf));
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
