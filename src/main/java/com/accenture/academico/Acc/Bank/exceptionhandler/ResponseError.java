package com.accenture.academico.Acc.Bank.exceptionhandler;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.accenture.academico.Acc.Bank.exception.NegocioException;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseError {

	private HttpStatus status;
	private String mensagem;
    private List<String> erros;

    public ResponseError(HttpStatus status, String mensagem) {
    	this.status = status;
    	this.mensagem = mensagem;
    }
    
	public ResponseError(HttpStatus httpStatus, String mensagem, String erro) {
		this.status = httpStatus;
		this.mensagem = mensagem;
		this.erros = Collections.singletonList(erro);
	}
    
    public ResponseError(NegocioException negocioException) {
    	this(negocioException.getHttpStatus(), negocioException.getMessage());
    }
}
