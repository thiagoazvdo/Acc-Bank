package com.accenture.academico.Acc.Bank.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequestDTO {

    @JsonProperty("valor")
    @NotNull(message = "Campo valor obrigatório")
	private BigDecimal valor;
    
    @JsonProperty("descricao")
	private String descricao;
    
    @JsonProperty("numeroContaDestino")
    @NotBlank(message = "Campo numeroContaDestino obrigatório")
    @Pattern(regexp = "\\d{5}", message = "Campo numeroContaDestino deve ter exatamente 5 digitos numericos")
	private String numeroContaDestino;
}
