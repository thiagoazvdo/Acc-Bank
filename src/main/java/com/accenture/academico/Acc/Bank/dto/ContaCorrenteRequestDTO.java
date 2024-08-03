package com.accenture.academico.Acc.Bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaCorrenteRequestDTO {

    @JsonProperty("idCliente")
    @NotNull(message = "Campo idCliente obrigatório")
	private Long idCliente;
    
    @JsonProperty("idAgencia")
    @NotNull(message = "Campo idAgencia obrigatório")
	private Long idAgencia;

}
