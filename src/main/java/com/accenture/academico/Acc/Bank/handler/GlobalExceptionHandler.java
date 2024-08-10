package com.accenture.academico.Acc.Bank.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.accenture.academico.Acc.Bank.exception.BancoException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Define o "manuseador" para quando, de qualquer parte da
     * Aplicação Web, uma exceção do tipo BancoException
     * for lançada.
     * 
     * @param bancoException
     * @return ResponseEntity
     */
    @ExceptionHandler(BancoException.class)
    public ResponseEntity<ResponseError> onBusinessException(BancoException bancoException) {
    	ResponseError responseError = new ResponseError(bancoException);
    	return ResponseEntity.status(bancoException.getHttpStatus()).body(responseError);
    }

    /*
    Daqui, abaixo, seguem os tratamentos dos erros oriundos das
    validações nos controladores: @Valid (jakarta.validation)
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    	List<String> erros = new ArrayList<>();
    	
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
        	erros.add(fieldError.getDefaultMessage());
        }
        
        ResponseError responseError = new ResponseError("Erros de validacao encontrados", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<?> tratarConexaoBancoDadosException(CannotCreateTransactionException e) {
        ResponseError responseError = new ResponseError("Falha na conexão com o banco de dados. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseError);
    }
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<?> tratarConexaoBancoDadosException(DataAccessResourceFailureException ex) {
        ResponseError responseError = new ResponseError("Falha na conexão com o banco de dados. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseError);

    }


}
