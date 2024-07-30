package com.accenture.academico.Acc.Bank.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.accenture.academico.Acc.Bank.dto.ContaCorrentePostRequestDTO;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.service.ContaCorrenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/conta-corrente", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContaCorrenteController {

	@Autowired
	private ContaCorrenteService contaCorrenteService;
	
	@PostMapping
	public ResponseEntity<?> criarContaCorrente(@Valid @RequestBody ContaCorrentePostRequestDTO contaDTO){
		ContaCorrente response = contaCorrenteService.criarContaCorrente(contaDTO);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.getId())
				.toUri())
				.body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarContaCorrente(@PathVariable Long id){
		return ResponseEntity.ok(contaCorrenteService.buscarContaCorrente(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarContaCorrente(@PathVariable Long id){
		contaCorrenteService.deletarContaCorrente(id);
		return ResponseEntity.noContent().build();
	}
	
	 @PutMapping("/{id}/sacar")
	 public ResponseEntity<?> sacar(@PathVariable Long id, @RequestParam BigDecimal valor) {
		 contaCorrenteService.sacar(id, valor);
		 return ResponseEntity.ok().build();
	 }
	 
	 @PutMapping("/{id}/depositar")
	 public ResponseEntity<?> depositar(@PathVariable Long id, @RequestParam BigDecimal valor) {
		 contaCorrenteService.depositar(id, valor);
		 return ResponseEntity.ok().build();
	 }
	 
	 @PutMapping("/{idOrigem}/transferir")
	 public ResponseEntity<?> transferir(@PathVariable Long idOrigem, @RequestParam Long idDestino, @RequestParam BigDecimal valor) {
		 contaCorrenteService.transferir(idOrigem, idDestino, valor);
		 return ResponseEntity.ok().build();
	 }
}
