package com.accenture.academico.Acc.Bank.controller;
import java.util.List;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.handler.ResponseBodyTemplate;
import com.accenture.academico.Acc.Bank.handler.ResponseHandler;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.service.AgenciaService;

@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    AgenciaService agenciaService;

    @GetMapping
    public ResponseEntity<?> listar() {
    	List<Agencia> listaAgencias = agenciaService.listarAgencias();
        return ResponseEntity.ok(listaAgencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
    	Agencia agencia = agenciaService.buscarAgencia(id);
		return ResponseEntity.ok(agencia);
    }

    @PutMapping("/{agenciaId}")
    public ResponseEntity<?> atualizar(@PathVariable Long agenciaId, @Valid @RequestBody AgenciaRequestDTO agenciaDTO) {
    	Agencia agencia = agenciaService.atualizarAgencia(agenciaId, agenciaDTO);
        return ResponseEntity.ok(agencia);
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@Valid @RequestBody AgenciaRequestDTO agenciaDTO){
    	Agencia agencia = agenciaService.criarAgencia(agenciaDTO);
        return ResponseEntity.ok(agencia);
    }

    @DeleteMapping("/{agenciaId}")
    public ResponseEntity<?> remover(@PathVariable Long agenciaId){
        agenciaService.removerAgencia(agenciaId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
