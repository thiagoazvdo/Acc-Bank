package com.accenture.academico.Acc.Bank.exception;

import org.springframework.http.HttpStatus;

public class ConexaoBancoDadosException extends BancoException {

    public ConexaoBancoDadosException(String mensagem) {
        super(mensagem);
        this.httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
    }

}
