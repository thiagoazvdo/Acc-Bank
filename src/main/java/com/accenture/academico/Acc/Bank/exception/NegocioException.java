package com.accenture.academico.Acc.Bank.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NegocioException extends RuntimeException {
    
	protected HttpStatus httpStatus;

    public NegocioException(String message) {
        super(message);
    }
}