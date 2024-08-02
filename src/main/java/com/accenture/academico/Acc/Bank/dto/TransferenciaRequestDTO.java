package com.accenture.academico.Acc.Bank.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Campo valor obrigatorio")
	private BigDecimal valor;
    
    @JsonProperty("descricao")
	private String descricao;
    
    @JsonProperty("idContaDestino")
    @NotNull(message = "Campo idContaDestino obrigatorio")
	private Long idContaDestino;
}
