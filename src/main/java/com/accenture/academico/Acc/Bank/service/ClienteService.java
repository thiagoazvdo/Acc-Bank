package com.accenture.academico.Acc.Bank.service;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }

    private boolean cpfJaCadastrado(String cpf) {
        return clienteRepository.findByCpf(cpf).isPresent();
    }

    public Cliente atualizar(Long clienteId, ClienteRequestDTO clienteRequestDTO){
        Cliente clienteAtual = buscarCliente(clienteId);
        clienteAtual.setNome(clienteRequestDTO.getNome());
        clienteAtual.setTelefone(clienteRequestDTO.getTelefone());
        clienteAtual.setCpf(clienteRequestDTO.getCpf());
        return clienteRepository.save(clienteAtual);
    }

    public Cliente criarCliente(ClienteRequestDTO clienteRequestDTO) {
        if (cpfJaCadastrado(clienteRequestDTO.getCpf())) throw new ClienteJaCadastradoException(clienteRequestDTO.getCpf());
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(clienteRequestDTO.getNome());
        novoCliente.setCpf(clienteRequestDTO.getCpf());
        novoCliente.setTelefone(clienteRequestDTO.getTelefone());
        return clienteRepository.save(novoCliente);
    }


    public void removerCliente(Long id) {
        clienteRepository.delete(buscarCliente(id));
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
}
