package com.accenture.academico.Acc.Bank.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.accenture.academico.Acc.Bank.exception.BancoException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

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
    public ResponseEntity<?> onBusinessException(BancoException bancoException) {
    	return ResponseHandler.error(bancoException);
    }

    /*
    Daqui, abaixo, seguem os tratamentos dos erros oriundos das
    validações nos controladores: @Valid (jakarta.validation)
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    	List<String> erros = new ArrayList<>();
    	
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
        	erros.add(fieldError.getDefaultMessage());
        }
        
    	return ResponseHandler.error("Erros de validacao encontrados", HttpStatus.BAD_REQUEST, erros);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> onConstraintViolation(ConstraintViolationException e) {
    	List<String> erros = new ArrayList<>();
    	
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            erros.add(violation.getMessage());
        }
        
    	return ResponseHandler.error("Erros de validacao encontrados", HttpStatus.BAD_REQUEST, erros);
    }
}
