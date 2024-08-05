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

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@GetMapping
	public ResponseEntity<?> listar() {
		List<Cliente> listaClientes = clienteService.listarClientes();
		return ResponseEntity.ok(listaClientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id) {
		Cliente cliente = clienteService.buscarCliente(id);
		return ResponseEntity.ok(cliente);
	}

	@PutMapping("/{clienteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long clienteId, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = clienteService.atualizar(clienteId, clienteRequestDTO);
		return ResponseEntity.ok(cliente);
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = clienteService.criarCliente(clienteRequestDTO);
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{clienteId}")
	public ResponseEntity<?> remover(@PathVariable Long clienteId) {
		clienteService.removerCliente(clienteId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
