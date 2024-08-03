package com.accenture.academico.Acc.Bank.service;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
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

    public Cliente atualizar(Long clienteId, ClienteRequestDTO clienteRequestDTO){
        Cliente clienteAtual = buscarCliente(clienteId);
        if(clienteRequestDTO.getNome() != null) clienteAtual.setNome(clienteRequestDTO.getNome());
        if(clienteRequestDTO.getCpf() != null) clienteAtual.setCpf(clienteRequestDTO.getCpf());
        if(clienteRequestDTO.getTelefone() != null) clienteAtual.setTelefone(clienteRequestDTO.getTelefone());
        return clienteRepository.save(clienteAtual);
    }

    public Cliente criarCliente(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteRequestDTO.toEntity();
        return clienteRepository.save(cliente);
    }

    public void removerCliente(Long id) {
        clienteRepository.delete(buscarCliente(id));
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
}
