package com.accenture.academico.Acc.Bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaCorrentePostRequestDTO {

    @JsonProperty("idCliente")
    @NotBlank(message = "Campo idCliente obrigatorio")
	private Long idCliente;
    
    @JsonProperty("idAgencia")
    @NotBlank(message = "Campo idAgencia obrigatorio")
	private Long idAgencia;

}
