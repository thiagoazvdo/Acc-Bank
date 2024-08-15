package com.accenture.academico.Acc.Bank.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Agência")
@RestController
@RequestMapping(value = "/agencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgenciaController {

    @Autowired
    AgenciaService agenciaService;

    @Operation(summary = "Adiciona uma nova agência.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agência criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Erro na requisição\", \"erros\": [\"Erro 01\", \"Erro 02\"]}"))),
    })
    @PostMapping
    public ResponseEntity<Agencia> adicionar(@Valid @RequestBody AgenciaRequestDTO agenciaDTO) {
        Agencia agencia = agenciaService.criarAgencia(agenciaDTO);
        URI uri = URI.create("/agencias/" + agencia.getId());
        return ResponseEntity.created(uri).body(agencia);
    }

    @Operation(summary = "Lista todas as agências.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma lista de todas as agências cadastradas.")
    })
    @GetMapping
    public ResponseEntity<List<Agencia>> listar() {
        List<Agencia> listaAgencias = agenciaService.listarAgencias();
        return ResponseEntity.ok(listaAgencias);
    }

    @Operation(summary = "Busca uma agência por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma Agência pelo ID."),
        @ApiResponse(responseCode = "404", description = "Agência não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Agencia nao encontrada\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Agencia> buscar(@Parameter(description = "ID da Agência a ser buscada") @PathVariable Long id) {
        Agencia agencia = agenciaService.buscarAgencia(id);
        return ResponseEntity.ok(agencia);
    }

    @Operation(summary = "Atualiza as informações de uma agência.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agência atualizada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Erro na requisição\", \"erros\": [\"Erro 01\", \"Erro 02\"]}"))),
        @ApiResponse(responseCode = "404", description = "Agência não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Agencia nao encontrada\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Agencia> atualizar(@PathVariable Long id, @Valid @RequestBody AgenciaRequestDTO agenciaDTO) {
        Agencia agencia = agenciaService.atualizarAgencia(id, agenciaDTO);
        return ResponseEntity.ok(agencia);
    }

    @Operation(summary = "Remove uma agência por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Agência removida com sucesso."),
        @ApiResponse(responseCode = "404", description = "Agência não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Agencia nao encontrada\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        agenciaService.removerAgencia(id);
        return ResponseEntity.noContent().build();
    }
}
