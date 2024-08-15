package com.accenture.academico.Acc.Bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.service.ContaCorrenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Conta Corrente")
@RestController
@RequestMapping(value = "/contas-correntes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContaCorrenteController {

    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @Operation(summary = "Busca uma conta corrente por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna a conta corrente com o ID especificado."),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Conta corrente não encontrada\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContaCorrente> buscarContaCorrente(
        @Parameter(description = "ID da conta corrente a ser buscada") @PathVariable Long id) {
        ContaCorrente contaCorrente = contaCorrenteService.buscarContaCorrente(id);
        return ResponseEntity.ok(contaCorrente);
    }

    @Operation(summary = "Lista todas as contas correntes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna uma lista de todas as contas correntes cadastradas.")
    })
    @GetMapping
    public ResponseEntity<List<ContaCorrente>> listarContas() {
        List<ContaCorrente> listaDeContas = contaCorrenteService.listarContas();
        return ResponseEntity.ok(listaDeContas);
    }

    @Operation(summary = "Realiza um saque na conta corrente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Erro na requisição\", \"erros\": [\"Erro 01\", \"Erro 02\"]}"))),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Conta corrente não encontrada\"}")))
    })
    @PostMapping("/{id}/sacar")
    public ResponseEntity<Void> sacar(
        @Parameter(description = "ID da conta corrente da qual será realizado o saque") @PathVariable Long id,
        @Valid @RequestBody SaqueDepositoRequestDTO saqueDTO) {
        contaCorrenteService.sacar(id, saqueDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Realiza um depósito na conta corrente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Erro na requisição\", \"erros\": [\"Erro 01\", \"Erro 02\"]}"))),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Conta corrente não encontrada\"}")))
    })
    @PostMapping("/{id}/depositar")
    public ResponseEntity<Void> depositar(
        @Parameter(description = "ID da conta corrente na qual será realizado o depósito") @PathVariable Long id,
        @Valid @RequestBody SaqueDepositoRequestDTO depositoDTO) {
        contaCorrenteService.depositar(id, depositoDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Realiza uma transferência entre contas correntes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Erro na requisição\", \"erros\": [\"Erro 01\", \"Erro 02\"]}"))),
        @ApiResponse(responseCode = "404", description = "Conta corrente de origem ou destino não encontrada.", content = @Content(examples = @ExampleObject(value = "{\"mensagem\": \"Conta corrente não encontrada\"}")))
    })
    @PostMapping("/{id}/transferir")
    public ResponseEntity<Void> transferir(
        @Parameter(description = "ID da conta corrente de origem da transferência") @PathVariable Long id,
        @Valid @RequestBody TransferenciaRequestDTO transferenciaDTO) {
        contaCorrenteService.transferir(id, transferenciaDTO);
        return ResponseEntity.ok().build();
    }
}
