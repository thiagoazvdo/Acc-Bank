package com.accenture.academico.Acc.Bank.exception.contacorrente;

import com.accenture.academico.Acc.Bank.exception.BancoException;
import org.springframework.http.HttpStatus;

public class ContaCorrenteJaCadastradoException extends BancoException {

    public ContaCorrenteJaCadastradoException(Long clienteId) {
        super(String.format("O cliente com id %d ja possui uma conta cadastrada", clienteId));
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
