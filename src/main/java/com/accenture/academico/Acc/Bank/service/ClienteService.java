package com.accenture.academico.Acc.Bank.service;

import com.accenture.academico.Acc.Bank.expection.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {


    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente buscarOuFalhar(Long clienteId) throws ClienteNaoEncontradoException {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNaoEncontradoException());
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> listar(){
        return clienteRepository.findAll();
    }

}
