package com.accenture.academico.Acc.Bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientePutRequestDTO {

	@NotBlank
	private String nome;

	@NotBlank
	@Pattern(regexp = "\\d{11}", message = "Cpf deve ter exatamente 11 digitos numericos")
	private String cpf;

	@NotBlank
	@Pattern(regexp = "\\d{11}", message = "Telefone deve ter exatamente 11 digitos numericos")
	private String telefone;
}
