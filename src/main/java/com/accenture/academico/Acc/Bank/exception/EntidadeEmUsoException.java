package com.accenture.academico.Acc.Bank.exception;

import org.springframework.http.HttpStatus;

public class EntidadeEmUsoException extends BancoException {
    public EntidadeEmUsoException(String mensagem) {
        super(mensagem);
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
