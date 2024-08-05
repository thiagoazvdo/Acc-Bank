package com.accenture.academico.Acc.Bank.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.service.TransacaoService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

	@Autowired
	private TransacaoService transacaoService;
	
	@GetMapping("/extrato-mensal")
	public ResponseEntity<List<Transacao>> extratoMensal(@RequestParam Long idConta, @RequestParam YearMonth mesAno) {
		List<Transacao> transacoes = transacaoService.obterExtratoMensal(idConta, mesAno);
		return ResponseEntity.ok(transacoes);
	}

	@GetMapping("/extrato-anual")
	public ResponseEntity<List<Transacao>> extratoAnual(@RequestParam Long idConta, @RequestParam int ano) {
		List<Transacao> transacoes = transacaoService.obterExtratoAnual(idConta, ano);
		return ResponseEntity.ok(transacoes);
	}

	@GetMapping("/extrato-filtrado")
	public ResponseEntity<List<Transacao>> extratoFiltrado(@RequestParam Long idConta,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime dataInicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime dataFim) {
		List<Transacao> transacoes = transacaoService.obterExtratoFiltrado(idConta, dataInicio, dataFim);
		return ResponseEntity.ok(transacoes);
	}
}
