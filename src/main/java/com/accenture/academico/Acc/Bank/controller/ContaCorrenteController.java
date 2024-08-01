package com.accenture.academico.Acc.Bank.controller;

import java.math.BigDecimal;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.dto.ContaCorrenteRequestDTO;
import com.accenture.academico.Acc.Bank.handler.ResponseHandler;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.service.ContaCorrenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas-correntes")
public class ContaCorrenteController {

	@Autowired
	private ContaCorrenteService contaCorrenteService;
	
	@PostMapping
	public ResponseEntity<?> criarContaCorrente(@Valid @RequestBody ContaCorrenteRequestDTO contaDTO){
		ContaCorrente contaCorrente = contaCorrenteService.criarContaCorrente(contaDTO);
		return ResponseHandler.success("Conta Corrente criada com sucesso.", contaCorrente, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarContaCorrente(@PathVariable Long id){
		ContaCorrente contaCorrente = contaCorrenteService.buscarContaCorrente(id);
		return ResponseHandler.success("Conta Corrente encontrada com sucesso.", contaCorrente, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerContaCorrente(@PathVariable Long id){
		contaCorrenteService.removerContaCorrente(id);
		return ResponseHandler.success("Conta Corrente removida com sucesso.", HttpStatus.NO_CONTENT);
	}
	
	 @PutMapping("/{id}/sacar")
	 public ResponseEntity<?> sacar(@PathVariable Long id, @RequestParam BigDecimal valor) {
		 contaCorrenteService.sacar(id, valor);
		 return ResponseHandler.success("Saque realizado com sucesso.", HttpStatus.OK);
	 }
	 
	 @PutMapping("/{id}/depositar")
	 public ResponseEntity<?> depositar(@PathVariable Long id, @RequestParam BigDecimal valor) {
		 contaCorrenteService.depositar(id, valor);
		 return ResponseHandler.success("Deposito realizado com sucesso.", HttpStatus.OK);
	 }
	 
	 @PutMapping("/{idOrigem}/transferir")
	 public ResponseEntity<?> transferir(@PathVariable Long idOrigem, @RequestParam Long idDestino, @RequestParam BigDecimal valor) {
		 contaCorrenteService.transferir(idOrigem, idDestino, valor);
		 return ResponseHandler.success("Transferência realizada com sucesso.", HttpStatus.OK);
	 }
}
