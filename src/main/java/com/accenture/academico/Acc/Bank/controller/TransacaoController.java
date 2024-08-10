package com.accenture.academico.Acc.Bank.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.service.TransacaoService;

@RestController
@RequestMapping
public class TransacaoController {

	@Autowired
	private TransacaoService transacaoService;
	
	@GetMapping("/contas-correntes/{idConta}/extrato-mensal")
	public ResponseEntity<List<Transacao>> extratoMensal(@PathVariable Long idConta, 
			@RequestParam @DateTimeFormat(pattern = "MM/yyyy") YearMonth mesAno) {
		List<Transacao> transacoes = transacaoService.obterExtratoMensal(idConta, mesAno);
		return ResponseEntity.ok(transacoes);
	}

	@GetMapping("/contas-correntes/{idConta}/extrato-anual")
	public ResponseEntity<List<Transacao>> extratoAnual(@PathVariable Long idConta, @RequestParam int ano) {
		List<Transacao> transacoes = transacaoService.obterExtratoAnual(idConta, ano);
		return ResponseEntity.ok(transacoes);
	}

	@GetMapping("/contas-correntes/{idConta}/extrato-filtrado")
	public ResponseEntity<List<Transacao>> extratoFiltrado(@PathVariable Long idConta,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataInicio,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataFim) {
		List<Transacao> transacoes = transacaoService.obterExtratoFiltrado(idConta, dataInicio, dataFim);
		return ResponseEntity.ok(transacoes);
	}
}
