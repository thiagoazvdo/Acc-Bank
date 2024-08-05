package com.accenture.academico.Acc.Bank.controller;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.service.AgenciaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    AgenciaService agenciaService;

    @GetMapping
    public ResponseEntity<List<Agencia>> listar() {
    	List<Agencia> listaAgencias = agenciaService.listarAgencias();
        return ResponseEntity.ok(listaAgencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agencia> buscar(@PathVariable Long id) {
    	Agencia agencia = agenciaService.buscarAgencia(id);
		return ResponseEntity.ok(agencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agencia> atualizar(@PathVariable Long id, @Valid @RequestBody AgenciaRequestDTO agenciaDTO) {
    	Agencia agencia = agenciaService.atualizarAgencia(id, agenciaDTO);
        return ResponseEntity.ok(agencia);
    }

    @PostMapping
    public ResponseEntity<Agencia> adicionar(@Valid @RequestBody AgenciaRequestDTO agenciaDTO){
    	Agencia agencia = agenciaService.criarAgencia(agenciaDTO);
    	URI uri = URI.create("/agencias/" + agencia.getId());
        return ResponseEntity.created(uri).body(agencia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        agenciaService.removerAgencia(id);
        return ResponseEntity.noContent().build();
    }

}
