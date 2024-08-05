package com.accenture.academico.Acc.Bank.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContaCorrenteRequestDTO {

    @NotNull(message = "Campo idCliente obrigatório")
	private Long idCliente;
    
    @NotNull(message = "Campo idAgencia obrigatório")
	private Long idAgencia;
}
