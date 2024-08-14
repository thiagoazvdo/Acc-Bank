package com.accenture.academico.Acc.Bank.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.service.TransacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/contas-correntes", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @Operation(summary = "Obtém o extrato geral de uma conta corrente.",
               description = "Retorna todas as transações associadas a uma conta corrente específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma lista de todas as transações da conta corrente."),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Conta corrente não encontrada\"}")))
    })
    @GetMapping("/{idConta}/extrato")
    public ResponseEntity<List<Transacao>> extrato(@Parameter(description = "ID da conta corrente") @PathVariable Long idConta) {
        List<Transacao> extrato = transacaoService.obterExtratoGeral(idConta);
        return ResponseEntity.ok(extrato);
    }

    @Operation(summary = "Obtém o extrato mensal de uma conta corrente.",
               description = "Retorna as transações de uma conta corrente específica para um determinado mês e ano.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma lista de transações do mês especificado."),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Conta corrente não encontrada\"}")))
    })
    @GetMapping("/{idConta}/extrato-mensal")
    public ResponseEntity<List<Transacao>> extratoMensal(
        @Parameter(description = "ID da conta corrente") @PathVariable Long idConta,
        @Parameter(description = "Mês e ano para o extrato no formato MM/yyyy") @RequestParam @DateTimeFormat(pattern = "MM/yyyy") YearMonth mesAno) {
        List<Transacao> extratoMensal = transacaoService.obterExtratoMensal(idConta, mesAno);
        return ResponseEntity.ok(extratoMensal);
    }

    @Operation(summary = "Obtém o extrato anual de uma conta corrente.",
               description = "Retorna as transações de uma conta corrente específica para um determinado ano.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma lista de transações do ano especificado."),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Conta corrente não encontrada\"}")))
    })
    @GetMapping("/{idConta}/extrato-anual")
    public ResponseEntity<List<Transacao>> extratoAnual(
        @Parameter(description = "ID da conta corrente") @PathVariable Long idConta,
        @Parameter(description = "Ano para o extrato") @RequestParam int ano) {
        List<Transacao> extratoAnual = transacaoService.obterExtratoAnual(idConta, ano);
        return ResponseEntity.ok(extratoAnual);
    }

    @Operation(summary = "Obtém o extrato filtrado por data de uma conta corrente.",
               description = "Retorna as transações de uma conta corrente específica entre duas datas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma lista de transações dentro do intervalo de datas especificado."),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Conta corrente não encontrada\"}")))
    })
    @GetMapping("/{idConta}/extrato-filtrado")
    public ResponseEntity<List<Transacao>> extratoFiltrado(
        @Parameter(description = "ID da conta corrente") @PathVariable Long idConta,
        @Parameter(description = "Data e hora de início do filtro no formato dd/MM/yyyy HH:mm") @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataInicio,
        @Parameter(description = "Data e hora de fim do filtro no formato dd/MM/yyyy HH:mm") @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataFim) {
        List<Transacao> extratoFiltrado = transacaoService.obterExtratoFiltrado(idConta, dataInicio, dataFim);
        return ResponseEntity.ok(extratoFiltrado);
    }
}
