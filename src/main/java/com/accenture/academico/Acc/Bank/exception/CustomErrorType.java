package com.accenture.academico.Acc.Bank.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorType {
	
	@JsonProperty("timestamp")
    private LocalDateTime timestamp;
	
	@JsonProperty("message")
    private String message;
    
	@JsonProperty("errors")
    private List<String> errors;
    
    public CustomErrorType(BancoException e) {
        this.timestamp = LocalDateTime.now();
        this.message = e.getMessage();
        this.errors = new ArrayList<>();
    }
}