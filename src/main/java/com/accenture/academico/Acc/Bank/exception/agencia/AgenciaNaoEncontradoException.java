package com.accenture.academico.Acc.Bank.exception.agencia;
import com.accenture.academico.Acc.Bank.exception.BancoException;

public class AgenciaNaoEncontradoException extends BancoException {
    public AgenciaNaoEncontradoException() {
        super("Não existe uma agencia cadastrada com o id informado");
    }
}
