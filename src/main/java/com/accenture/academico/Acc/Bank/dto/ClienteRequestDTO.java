package com.accenture.academico.Acc.Bank.dto;


import com.accenture.academico.Acc.Bank.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteRequestDTO {

    @NotBlank(message = "Campo nome obrigatório!")
    private String nome;

    @NotBlank(message = "Campo Cpf obrigatório!")
    private String cpf;

    @NotBlank(message = "Campo telefone obrigatório!")
    private String telefone;

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setTelefone(this.telefone);
        return cliente;
    }

}
