package com.accenture.academico.Acc.Bank.handler;

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
public class ResponseBodyTemplate {

	@JsonProperty("data")
	private Object data;
	
	@JsonProperty("success")
	private boolean success;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("status")
	private int status;
	
	@JsonProperty("errors")
    private List<String> errors;
}
