package com.accenture.academico.Acc.Bank.exception;

public class BancoException extends RuntimeException {
    public BancoException() {
        super("Erro inesperado no Acc Bank!");
    }

    public BancoException(String message) {
        super(message);
    }
}