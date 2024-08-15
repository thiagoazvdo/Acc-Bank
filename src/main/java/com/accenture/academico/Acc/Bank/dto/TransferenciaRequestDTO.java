package com.accenture.academico.Acc.Bank.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferenciaRequestDTO {

    @NotNull
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
	private BigDecimal valor;
    
	private String descricao;
    
    @NotBlank
	private String numeroContaDestino;
}
