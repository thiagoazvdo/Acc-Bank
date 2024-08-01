package com.accenture.academico.Acc.Bank.controller;
import java.util.List;

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
    public ResponseEntity<ResponseBodyTemplate> listar() {
    	List<Agencia> listaAgencias = agenciaService.listarAgencias();
		return ResponseHandler.success("Listagem de agencias concluida com sucesso.", listaAgencias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBodyTemplate> buscar(@PathVariable Long id) {
    	Agencia agencia = agenciaService.buscarAgencia(id);
		return ResponseHandler.success("Agencia encontrado com sucesso.", agencia, HttpStatus.OK);
    }

    @PutMapping("/{agenciaId}")
    public ResponseEntity<ResponseBodyTemplate> atualizar(@PathVariable Long agenciaId, @RequestBody Agencia agenciaDTO) {
    	Agencia agencia = agenciaService.atualizarAgencia(agenciaId, agenciaDTO);
		return ResponseHandler.success("Agencia atualizada com sucesso.", agencia, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseBodyTemplate> adicionar(@RequestBody Agencia agenciaDTO){
    	Agencia agencia = agenciaService.criarAgencia(agenciaDTO);
		return ResponseHandler.success("Agencia criada com sucesso.", agencia, HttpStatus.CREATED);
    }

    @DeleteMapping("/{agenciaId}")
    public ResponseEntity<ResponseBodyTemplate> remover(@PathVariable Long agenciaId){
        agenciaService.removerAgencia(agenciaId);
        return ResponseHandler.success("Agencia removida com sucesso.", HttpStatus.NO_CONTENT);
    }

}
