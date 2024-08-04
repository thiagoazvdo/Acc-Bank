package com.accenture.academico.Acc.Bank.exception.contacorrente;

import com.accenture.academico.Acc.Bank.exception.BancoException;
import org.springframework.http.HttpStatus;

public class ContaCorrenteJaCadastradoException extends BancoException {

    public ContaCorrenteJaCadastradoException(Long contaId) {
        super(String.format("Já existe uma conta corrente cadastrada com o id %d", contaId));
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
