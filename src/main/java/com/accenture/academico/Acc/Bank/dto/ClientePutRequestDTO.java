package com.accenture.academico.Acc.Bank.dto;

import com.accenture.academico.Acc.Bank.validation.ValidCPF;
import com.accenture.academico.Acc.Bank.validation.ValidEmail;
import com.accenture.academico.Acc.Bank.validation.ValidTelefone;

import jakarta.validation.constraints.NotBlank;
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

	@ValidCPF
	@NotBlank
    private String cpf;

	@ValidTelefone
	@NotBlank
	private String telefone;
	
	@ValidEmail
    @NotBlank
    private String email;
}
