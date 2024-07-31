package com.accenture.academico.Acc.Bank.controller;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;


    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscarCliente(id));
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cliente cliente){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvar(cliente));

    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long clienteId, @RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.atualizar(clienteId, cliente));
    }


    @DeleteMapping("/{clienteId}")
    public ResponseEntity<?> remover(@PathVariable Long clienteId){
        clienteService.excluir(clienteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
