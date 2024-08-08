package com.accenture.academico.Acc.Bank.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BancoException extends RuntimeException {
    
	protected HttpStatus httpStatus;

    public BancoException(String message) {
        super(message);
    }
}