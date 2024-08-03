package com.accenture.academico.Acc.Bank.dto;


import com.accenture.academico.Acc.Bank.model.Agencia;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgenciaRequestDTO {

	@JsonProperty("nome")
    @NotBlank(message = "Campo nome obrigatório!")
    private String nome;

	@JsonProperty("endereco")
    @NotBlank(message = "Campo endereco obrigatório!")
    private String endereco;

	@JsonProperty("telefone")
    @NotBlank(message = "Campo telefone obrigatório!")
    private String telefone;

    public Agencia toEntity() {
        Agencia agencia = new Agencia();
        agencia.setNome(this.nome);
        agencia.setEndereco(this.endereco);
        agencia.setTelefone(this.telefone);
        return agencia;
    }

}
