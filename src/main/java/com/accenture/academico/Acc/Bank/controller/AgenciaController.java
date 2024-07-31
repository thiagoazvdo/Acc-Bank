package com.accenture.academico.Acc.Bank.controller;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agencia")
public class AgenciaController {

    @Autowired
    AgenciaService agenciaService;


    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(agenciaService.listarAgencias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(agenciaService.buscarAgencia(id));
    }


    @PutMapping("/{AgenciaId}")
    public ResponseEntity<?> atualizar(@PathVariable Long agenciaId, @RequestBody Agencia agencia) {
        return ResponseEntity.status(HttpStatus.OK).body(agenciaService.atualizarAgencia(agenciaId, agencia));
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Agencia agencia){
        return ResponseEntity.status(HttpStatus.CREATED).body(agenciaService.criarAgencia(agencia));

    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<?> remover(@PathVariable Long clienteId){
        clienteService.removerCliente(clienteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
