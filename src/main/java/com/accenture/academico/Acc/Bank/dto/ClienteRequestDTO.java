package com.accenture.academico.Acc.Bank.dto;


import com.accenture.academico.Acc.Bank.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteRequestDTO {

	@JsonProperty("nome")
    @NotBlank(message = "Campo nome obrigatório")
    private String nome;

	@JsonProperty("cpf")
    @Pattern(regexp = "\\d{11}", message = "Cpf deve ter exatamente 11 digitos numericos")
    @NotBlank(message = "Campo cpf obrigatório!")
    private String cpf;

	@JsonProperty("telefone")
    @NotBlank(message = "Campo telefone obrigatório")
    private String telefone;

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setTelefone(this.telefone);
        return cliente;
    }

}
