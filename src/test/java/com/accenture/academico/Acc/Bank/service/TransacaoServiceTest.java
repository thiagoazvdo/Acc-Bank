package com.accenture.academico.Acc.Bank.service;

import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.TipoTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.repository.TransacaoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Testes do service de Transações")
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private ContaCorrenteService contaCorrenteService;

    @Mock
    private TransacaoRepository transacaoRepository;

    private Transacao transacao;
    private ContaCorrente contaCorrente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        contaCorrente = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
        transacao = new Transacao();
        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor(BigDecimal.valueOf(100.00));
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao("Depósito inicial");
        transacao.setContaCorrente(contaCorrente);
    }

    @Test
    @DisplayName("Testa obter extrato geral com sucesso")
    void testObterExtratoGeral_Sucesso() {
        // Arrange
        when(contaCorrenteService.buscarContaCorrente(contaCorrente.getId())).thenReturn(contaCorrente);
        when(transacaoRepository.findByContaCorrente(eq(contaCorrente))).thenReturn(Collections.singletonList(transacao));

        // Act
        List<Transacao> resultado = transacaoService.obterExtratoGeral(contaCorrente.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao, resultado.get(0));
    }
    
    @Test
    @DisplayName("Testa obter extrato mensal com sucesso")
    void testObterExtratoMensal_Sucesso() {
        // Arrange
        YearMonth mesAno = YearMonth.now();
        LocalDateTime mesInicio = mesAno.atDay(1).atStartOfDay();
        LocalDateTime mesFim = mesAno.atEndOfMonth().atTime(23, 59, 59);
        when(contaCorrenteService.buscarContaCorrente(anyLong())).thenReturn(contaCorrente);
        when(transacaoRepository.findByContaCorrenteAndDataHoraBetween(eq(contaCorrente), eq(mesInicio), eq(mesFim)))
                .thenReturn(Collections.singletonList(transacao));

        // Act
        List<Transacao> resultado = transacaoService.obterExtratoMensal(1L, mesAno);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao, resultado.get(0));
    }

    @Test
    @DisplayName("Testa obter extrato anual com sucesso")
    void testObterExtratoAnual_Sucesso() {
        // Arrange
        int ano = 2024;
        LocalDateTime anoInicio = LocalDateTime.of(ano, 1, 1, 0, 0);
        LocalDateTime anoFim = LocalDateTime.of(ano, 12, 31, 23, 59);
        when(contaCorrenteService.buscarContaCorrente(anyLong())).thenReturn(contaCorrente);
        when(transacaoRepository.findByContaCorrenteAndDataHoraBetween(eq(contaCorrente), eq(anoInicio), eq(anoFim)))
                .thenReturn(Collections.singletonList(transacao));

        // Act
        List<Transacao> resultado = transacaoService.obterExtratoAnual(1L, ano);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao, resultado.get(0));
    }

    @Test
    @DisplayName("Testa obter extrato filtrado com sucesso")
    void testObterExtratoFiltrado_Sucesso() {
        // Arrange
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(7);
        LocalDateTime dataFim = LocalDateTime.now();
        when(contaCorrenteService.buscarContaCorrente(anyLong())).thenReturn(contaCorrente);
        when(transacaoRepository.findByContaCorrenteAndDataHoraBetween(eq(contaCorrente), eq(dataInicio), eq(dataFim)))
                .thenReturn(Collections.singletonList(transacao));

        // Act
        List<Transacao> resultado = transacaoService.obterExtratoFiltrado(1L, dataInicio, dataFim);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao, resultado.get(0));
    }

    @Test
    @DisplayName("Testa obter extrato filtrado quando conta não encontrada")
    void testObterExtratoFiltrado_ContaNaoEncontrada() {
        // Arrange
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(7);
        LocalDateTime dataFim = LocalDateTime.now();
        Long idInexistente = 9999L;
        when(contaCorrenteService.buscarContaCorrente(idInexistente)).thenThrow(new ContaCorrenteNaoEncontradaException(idInexistente));

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> transacaoService.obterExtratoFiltrado(idInexistente, dataInicio, dataFim));
        verify(contaCorrenteService, times(1)).buscarContaCorrente(idInexistente);
    }
}