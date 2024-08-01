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
import com.accenture.academico.Acc.Bank.handler.ResponseBodyTemplate;
import com.accenture.academico.Acc.Bank.handler.ResponseHandler;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@GetMapping
	public ResponseEntity<ResponseBodyTemplate> listar() {
		List<Cliente> listaClientes = clienteService.listarClientes();
		return ResponseHandler.success("Listagem de clientes concluida com sucesso.", listaClientes, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseBodyTemplate> buscar(@PathVariable Long id) {
		Cliente cliente = clienteService.buscarCliente(id);
		return ResponseHandler.success("Cliente encontrado com sucesso.", cliente, HttpStatus.OK);
	}

	@PutMapping("/{clienteId}")
	public ResponseEntity<ResponseBodyTemplate> atualizar(@PathVariable Long clienteId, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = clienteService.atualizar(clienteId, clienteRequestDTO);
		return ResponseHandler.success("Cliente atualizado com sucesso.", cliente, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseBodyTemplate> adicionar(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = clienteService.criarCliente(clienteRequestDTO);
		return ResponseHandler.success("Cliente criado com sucesso.", cliente, HttpStatus.CREATED);
	}

	@DeleteMapping("/{clienteId}")
	public ResponseEntity<ResponseBodyTemplate> remover(@PathVariable Long clienteId) {
		clienteService.removerCliente(clienteId);
		return ResponseHandler.success("Cliente removido com sucesso.", HttpStatus.NO_CONTENT);
	}

}
