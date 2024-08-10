package com.accenture.academico.Acc.Bank.handler;

import java.util.List;

import com.accenture.academico.Acc.Bank.exception.BancoException;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
public class ResponseError {

	private String message;
    private List<String> errors;

    public ResponseError(String message) {
    	this.message = message;
    }
    
    public ResponseError(BancoException bancoException) {
    	this(bancoException.getMessage());
    }
}
