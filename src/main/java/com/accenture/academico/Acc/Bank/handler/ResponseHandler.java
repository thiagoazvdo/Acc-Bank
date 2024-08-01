package com.accenture.academico.Acc.Bank.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.accenture.academico.Acc.Bank.exception.BancoException;

public class ResponseHandler {

	private static ResponseBodyTemplate criarResponseBodyTemplate(Object data, boolean success, String message, HttpStatus status, List<String> errors) {
        return ResponseBodyTemplate.builder()
                .data(data)
                .success(success)
                .message(message)
                .status(status.value())
                .errors(errors)
                .build();
    }

    public static ResponseEntity<ResponseBodyTemplate> success(String message, Object data, HttpStatus status) {
    	return ResponseEntity.status(status)
                .body(criarResponseBodyTemplate(data, true, message, status, null));
    }
    
    public static ResponseEntity<ResponseBodyTemplate> success(String message, HttpStatus status) {
    	return success(message, null, status);
    }

    public static ResponseEntity<ResponseBodyTemplate> error(String message, HttpStatus status, List<String> errors) {
    	return ResponseEntity.status(status)
                .body(criarResponseBodyTemplate(null, false, message, status, errors));
    }
    
    public static ResponseEntity<ResponseBodyTemplate> error(BancoException e) {
    	return error(e.getMessage(), e.getHttpStatus(), null);
    }
}
