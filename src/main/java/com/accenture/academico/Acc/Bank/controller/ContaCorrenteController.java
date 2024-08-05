package com.accenture.academico.Acc.Bank.controller;

import com.accenture.academico.Acc.Bank.dto.ContaCorrenteResponse;
import org.modelmapper.ModelMapper;
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
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.service.ContaCorrenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas-correntes")
public class ContaCorrenteController {

	@Autowired
	private ContaCorrenteService contaCorrenteService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	public ResponseEntity<?> criarContaCorrente(@Valid @RequestBody ContaCorrenteRequestDTO contaDTO){
		ContaCorrente contaCorrente = contaCorrenteService.criarContaCorrente(contaDTO);
		return ResponseEntity.ok(contaCorrente);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarContaCorrente(@PathVariable Long id){
		ContaCorrente contaCorrente = contaCorrenteService.buscarContaCorrente(id);
		ContaCorrenteResponse contaCorrenteResponse = modelMapper.map(contaCorrente, ContaCorrenteResponse.class);
		return ResponseEntity.ok(contaCorrenteResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerContaCorrente(@PathVariable Long id){
		contaCorrenteService.removerContaCorrente(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/{id}/sacar")
	public ResponseEntity<?> sacar(@PathVariable Long id, @Valid @RequestBody SaqueDepositoRequestDTO saqueDTO) {
		contaCorrenteService.sacar(id, saqueDTO);
		return ResponseEntity.ok(saqueDTO);
	}

	@PostMapping("/{id}/depositar")
	public ResponseEntity<?> depositar(@PathVariable Long id, @Valid @RequestBody SaqueDepositoRequestDTO depositoDTO) {
		contaCorrenteService.depositar(id, depositoDTO);
		return ResponseEntity.ok(depositoDTO);
	}

	@PostMapping("/{idOrigem}/transferir")
	public ResponseEntity<?> transferir(@PathVariable Long idOrigem, @Valid @RequestBody TransferenciaRequestDTO transferenciaDTO) {
		contaCorrenteService.transferir(idOrigem, transferenciaDTO);
		return ResponseEntity.ok(transferenciaDTO);
	}
	 
}
