package com.accenture.academico.Acc.Bank.handler;

import java.util.ArrayList;
import java.util.List;

import com.accenture.academico.Acc.Bank.exception.BancoException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseError {

	private String message;
    private List<String> errors;
	
    public ResponseError(BancoException bancoException) {
    	this.message = bancoException.getMessage();
    	this.errors = new ArrayList<>();
    }
}
