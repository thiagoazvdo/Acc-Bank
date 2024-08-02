package com.accenture.academico.Acc.Bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.dto.ContaCorrenteRequestDTO;
import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.handler.ResponseBodyTemplate;
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
	public ResponseEntity<ResponseBodyTemplate> criarContaCorrente(@Valid @RequestBody ContaCorrenteRequestDTO contaDTO){
		ContaCorrente contaCorrente = contaCorrenteService.criarContaCorrente(contaDTO);
		return ResponseHandler.success("Conta Corrente criada com sucesso.", contaCorrente, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseBodyTemplate> buscarContaCorrente(@PathVariable Long id){
		ContaCorrente contaCorrente = contaCorrenteService.buscarContaCorrente(id);
		return ResponseHandler.success("Conta Corrente encontrada com sucesso.", contaCorrente, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseBodyTemplate> removerContaCorrente(@PathVariable Long id){
		contaCorrenteService.removerContaCorrente(id);
		return ResponseHandler.success("Conta Corrente removida com sucesso.", HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/{id}/sacar")
	 public ResponseEntity<ResponseBodyTemplate> sacar(@PathVariable Long id, @Valid @RequestBody SaqueDepositoRequestDTO saqueDTO) {
		 contaCorrenteService.sacar(id, saqueDTO);
		 return ResponseHandler.success("Saque realizado com sucesso.", HttpStatus.OK);
	 }
	 
	 @PostMapping("/{id}/depositar")
	 public ResponseEntity<ResponseBodyTemplate> depositar(@PathVariable Long id, @Valid @RequestBody SaqueDepositoRequestDTO depositoDTO) {
		 contaCorrenteService.depositar(id, depositoDTO);
		 return ResponseHandler.success("Deposito realizado com sucesso.", HttpStatus.OK);
	 }
	 
	 @PostMapping("/{idOrigem}/transferir")
	 public ResponseEntity<ResponseBodyTemplate> transferir(@PathVariable Long idOrigem, @Valid @RequestBody TransferenciaRequestDTO transferenciaDTO) {
		 contaCorrenteService.transferir(idOrigem, transferenciaDTO);
		 return ResponseHandler.success("Transferência realizada com sucesso.", HttpStatus.OK);
	 }
	 
}
