package com.accenture.academico.Acc.Bank.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ModelMapper modelMapper;
	
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }

    private boolean cpfJaCadastrado(String cpf) {
        return clienteRepository.findByCpf(cpf).isPresent();
    }

    public Cliente atualizar(Long clienteId, ClienteRequestDTO clienteRequestDTO){
        Cliente cliente = buscarCliente(clienteId);
        
        Cliente clienteAtualizado = converterParaCliente(clienteRequestDTO);
        clienteAtualizado.setId(cliente.getId());
        
        return clienteRepository.save(clienteAtualizado);
    }

    public Cliente criarCliente(ClienteRequestDTO clienteRequestDTO) {
        if (cpfJaCadastrado(clienteRequestDTO.getCpf())) 
        	throw new ClienteJaCadastradoException(clienteRequestDTO.getCpf());
        
        Cliente novoCliente = converterParaCliente(clienteRequestDTO);
        return clienteRepository.save(novoCliente);
    }


    public void removerCliente(Long id) {
        clienteRepository.delete(buscarCliente(id));
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
    
    private Cliente converterParaCliente(ClienteRequestDTO clienteDTO) {
    	return modelMapper.map(clienteDTO, Cliente.class);
    }
}
