package com.accenture.academico.Acc.Bank.exceptionhandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.accenture.academico.Acc.Bank.exception.NegocioException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ResponseError> handleBusinessException(NegocioException negocioException) {
    	
    	ResponseError responseError = new ResponseError(negocioException);
    	return ResponseEntity.status(responseError.getStatus()).body(responseError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        
    	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    	String mensagem = "Erros de validação encontrados.";
    	
    	BindingResult bindingResult = ex.getBindingResult();
        List<String> erros = bindingResult.getFieldErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                .toList();

        ResponseError responseError = new ResponseError(httpStatus, mensagem, erros);
        return ResponseEntity.status(httpStatus).body(responseError);
    }
    
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ResponseError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        
    	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    	String mensagem = ex.getLocalizedMessage();
    	String erro = ex.getName() + " deve ser do tipo " + ex.getRequiredType().getName();

        ResponseError responseError = new ResponseError(httpStatus, mensagem, erro);
        return ResponseEntity.status(httpStatus).body(responseError);
    }
    
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ResponseError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String mensagem = "O corpo da requisição não pode ser lido.";
        String erro = ex.getMessage();

        ResponseError responseError = new ResponseError(httpStatus, mensagem, erro);
        return ResponseEntity.status(httpStatus).body(responseError);
    }

    @ExceptionHandler({CannotCreateTransactionException.class, DataAccessResourceFailureException.class})
    public ResponseEntity<ResponseError> handleConexaoBancoDadosException(RuntimeException ex) {

    	HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
    	String mensagem = "Falha na conexão com o banco de dados. Tente novamente mais tarde.";
    	ex.printStackTrace();
        ResponseError responseError = new ResponseError(httpStatus, mensagem);
        return ResponseEntity.status(httpStatus).body(responseError);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ResponseError> handleAll(Exception ex) {
    	
    	HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    	String mensagem = ex.getMessage();
    	
    	ResponseError responseError = new ResponseError(httpStatus, mensagem);
        return ResponseEntity.status(httpStatus).body(responseError);
    }

}
