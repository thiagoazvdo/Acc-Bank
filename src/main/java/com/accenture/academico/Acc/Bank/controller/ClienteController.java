package com.accenture.academico.Acc.Bank.controller;
import com.accenture.academico.Acc.Bank.expection.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.service.ClienteService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;


    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) throws ClienteNaoEncontradoException {
        return clienteService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente adicionar(@RequestBody Cliente cliente){
        System.out.println("CLIENTE RECEBIDO: " + cliente);
        return clienteService.salvar(cliente);
    }

    @PutMapping("/{clienteId}")
    public Cliente atualizar(@PathVariable Long clienteId, @RequestBody Cliente cliente) throws ClienteNaoEncontradoException{
        Cliente clienteAtual = clienteService.buscarOuFalhar(clienteId);
        clienteAtual.setNome(cliente.getNome());
        if (cliente.getTelefone() != null) {
            clienteAtual.setTelefone(cliente.getTelefone());
        }
        return clienteService.salvar(clienteAtual);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{clienteId}")
    public void remover(@PathVariable Long clienteId){
        clienteService.excluir(clienteId);
    }


}
