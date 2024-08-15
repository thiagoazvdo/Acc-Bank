package com.accenture.academico.Acc.Bank.exception.cliente;

import com.accenture.academico.Acc.Bank.exception.NegocioException;
import org.springframework.http.HttpStatus;

public class ClienteJaCadastradoException extends NegocioException {

    public ClienteJaCadastradoException(String campo, String cpf) {
        super(String.format("Já existe um cliente cadastrado com o %s %s.", campo, cpf));
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
