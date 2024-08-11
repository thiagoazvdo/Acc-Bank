package com.accenture.academico.Acc.Bank.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.dto.ContaCorrenteResponseDTO;
import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.service.ContaCorrenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas-correntes")
public class ContaCorrenteController {

	@Autowired
	private ContaCorrenteService contaCorrenteService;

	@GetMapping("/{id}")
	public ResponseEntity<ContaCorrenteResponseDTO> buscarContaCorrente(@PathVariable Long id){
		ContaCorrente contaCorrente = contaCorrenteService.buscarContaCorrente(id);
		ContaCorrenteResponseDTO contaCorrenteResponse = new ContaCorrenteResponseDTO();
		BeanUtils.copyProperties(contaCorrente, contaCorrenteResponse);
		
		return ResponseEntity.ok(contaCorrenteResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removerContaCorrente(@PathVariable Long id){
		contaCorrenteService.removerContaCorrente(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/sacar")
	public ResponseEntity<Void> sacar(@PathVariable Long id, @Valid @RequestBody SaqueDepositoRequestDTO saqueDTO) {
		contaCorrenteService.sacar(id, saqueDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/depositar")
	public ResponseEntity<Void> depositar(@PathVariable Long id, @Valid @RequestBody SaqueDepositoRequestDTO depositoDTO) {
		contaCorrenteService.depositar(id, depositoDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{idOrigem}/transferir")
	public ResponseEntity<Void> transferir(@PathVariable Long idOrigem, @Valid @RequestBody TransferenciaRequestDTO transferenciaDTO) {
		contaCorrenteService.transferir(idOrigem, transferenciaDTO);
		return ResponseEntity.ok().build();
	}
	 
}
