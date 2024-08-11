package com.accenture.academico.Acc.Bank.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "\\d{5}", message = "Campo numeroContaDestino deve ter exatamente 5 digitos numericos")
	private String numeroContaDestino;
}
