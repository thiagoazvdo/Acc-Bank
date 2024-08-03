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
public class SaqueDepositoRequestDTO {

    @JsonProperty("valor")
    @NotNull(message = "Campo valor obrigatório")
	private BigDecimal valor;
    
    @JsonProperty("descricao")
	private String descricao;
}
