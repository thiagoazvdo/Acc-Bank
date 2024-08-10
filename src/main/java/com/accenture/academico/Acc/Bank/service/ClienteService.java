package com.accenture.academico.Acc.Bank.service;

import java.util.List;

import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;

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

    private boolean cpfJaCadastrado(String cpf) {
        return clienteRepository.findByCpf(cpf).isPresent();
    }

    public Cliente atualizar(Long clienteId, ClienteRequestDTO clienteRequestDTO){
        Cliente cliente = buscarCliente(clienteId);
        BeanUtils.copyProperties(clienteRequestDTO, cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente criarCliente(ClienteRequestDTO clienteRequestDTO) {

        if (cpfJaCadastrado(clienteRequestDTO.getCpf())) 
        	throw new ClienteJaCadastradoException(clienteRequestDTO.getCpf());

        Agencia agencia = agenciaService.buscarAgencia(clienteRequestDTO.getIdAgencia());
        
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteRequestDTO, cliente);
        cliente.setAgencia(agencia);

        contaCorrenteService.criarContaCorrente(cliente);

        return clienteRepository.save(cliente);
    }


    public void removerCliente(Long id) {
        clienteRepository.delete(buscarCliente(id));
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
}
