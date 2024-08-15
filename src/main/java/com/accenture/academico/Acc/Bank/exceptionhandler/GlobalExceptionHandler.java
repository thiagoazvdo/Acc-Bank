package com.accenture.academico.Acc.Bank.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.accenture.academico.Acc.Bank.exception.NegocioException;

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
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ResponseError> onBusinessException(NegocioException negocioException) {
    	ResponseError responseError = new ResponseError(negocioException);
    	return ResponseEntity.status(negocioException.getHttpStatus()).body(responseError);
    }

    /*
    Daqui, abaixo, seguem os tratamentos dos erros oriundos das
    validações nos controladores: @Valid (jakarta.validation)
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<String> erros = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    return messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                })
                .toList();

        ResponseError responseError = new ResponseError("Erros de validacao encontrados", erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<?> tratarConexaoBancoDadosException(CannotCreateTransactionException e) {
        ResponseError responseError = new ResponseError("Falha na conexao com o banco de dados. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseError);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<?> tratarConexaoBancoDadosException(DataAccessResourceFailureException ex) {
        ResponseError responseError = new ResponseError("Falha na conexao com o banco de dados. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseError);

    }


}
