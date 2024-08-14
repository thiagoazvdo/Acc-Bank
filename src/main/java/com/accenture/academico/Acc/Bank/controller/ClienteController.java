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

import com.accenture.academico.Acc.Bank.dto.ClientePutRequestDTO;
import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cliente")
@RestController
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@Operation(summary = "Adiciona um novo cliente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(schema = @Schema(implementation = Cliente.class))),
	        @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Erro na requisição\", \"errors\": [\"Erro 01\", \"Erro 02\"]}"))) 
	})
	@PostMapping
	public ResponseEntity<Cliente> adicionarCliente(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = clienteService.criarCliente(clienteRequestDTO);
		URI uri = URI.create("/clientes/" + cliente.getId());
		return ResponseEntity.created(uri).body(cliente);
	}

	@Operation(summary = "Lista todos os clientes.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado", content = @Content) 
	})
	@GetMapping
	public ResponseEntity<List<Cliente>> listar() {
		List<Cliente> listaClientes = clienteService.listarClientes();
		return ResponseEntity.ok(listaClientes);
	}

	@Operation(summary = "Busca um cliente pelo ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente retornado com sucesso", content = @Content(schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Cliente não encontrado\"}"))) 
	})
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
		Cliente cliente = clienteService.buscarCliente(id);
		return ResponseEntity.ok(cliente);
	}

	@Operation(summary = "Atualiza as informações de um cliente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Erro na requisição\", \"errors\": [\"Erro 01\", \"Erro 02\"]}"))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Cliente não encontrado\"}")))  
	})
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
			@Valid @RequestBody ClientePutRequestDTO clientePutRequestDTO) {
		Cliente cliente = clienteService.atualizar(id, clientePutRequestDTO);
		return ResponseEntity.ok(cliente);
	}

	@Operation(summary = "Remove um cliente pelo ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cliente removido com sucesso", content = @Content),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Cliente não encontrado\"}"))) 
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		clienteService.removerCliente(id);
		return ResponseEntity.noContent().build();
	}
}
