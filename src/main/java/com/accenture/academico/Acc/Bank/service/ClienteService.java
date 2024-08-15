package com.accenture.academico.Acc.Bank.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.academico.Acc.Bank.dto.ClientePutRequestDTO;
import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private AgenciaService agenciaService;
    
    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }

    public Cliente atualizar(Long clienteId, ClientePutRequestDTO clientePutRequestDTO){
        Cliente cliente = buscarCliente(clienteId);
        BeanUtils.copyProperties(clientePutRequestDTO, cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente criarCliente(ClienteRequestDTO clienteRequestDTO) {
    	verificaSeCpfJaCadastrado(clienteRequestDTO.getCpf());
    	verificaSeTelefoneJaCadastrado(clienteRequestDTO.getTelefone());
    	
        Agencia agencia = agenciaService.buscarAgencia(clienteRequestDTO.getIdAgencia());
        
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteRequestDTO, cliente);
        cliente.setAgencia(agencia);
        clienteRepository.save(cliente);
        
        cliente.setContaCorrente(new ContaCorrente(cliente));

        return clienteRepository.save(cliente);
    }

    public void removerCliente(Long id) {
    	Cliente cliente = buscarCliente(id);
    	contaCorrenteService.removerContaCorrente(cliente.getContaCorrente().getId());
    	
        clienteRepository.delete(cliente);
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
    
    private void verificaSeCpfJaCadastrado(String cpf) {
    	if (clienteRepository.existsByCpf(cpf)) 
			throw new ClienteJaCadastradoException("cpf", cpf);
    }
    
    private void verificaSeTelefoneJaCadastrado(String telefone) {
    	if (clienteRepository.existsByTelefone(telefone)) 
			throw new ClienteJaCadastradoException("telefone", telefone);
    }
}
