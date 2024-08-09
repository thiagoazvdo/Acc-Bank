package com.accenture.academico.Acc.Bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.TipoTransacao;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.accenture.academico.Acc.Bank.repository.ContaCorrenteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do controller de Transações")
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Transacao transacao1;
    private Transacao transacao2;
    private ContaCorrente contaCorrente;
    private YearMonth mesAno;

    @BeforeEach
    void setUp() {
        // Inicializa os objetos de teste
        Agencia agencia1 = agenciaRepository.save(new Agencia(null, "Agencia 1", "Endereco 1", "123456789"));

        contaCorrente = new ContaCorrente(1L, "10001", BigDecimal.ZERO, agencia1, null, null);
        contaCorrenteRepository.save(contaCorrente);

        transacao1 = new Transacao(1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100.00), LocalDateTime.now().minusDays(10), "Depósito Inicial", contaCorrente, null);
        transacao2 = new Transacao(2L, TipoTransacao.SAQUE, BigDecimal.valueOf(50.00), LocalDateTime.now().minusDays(5), "Saque", contaCorrente, null);

        mesAno = YearMonth.now();
    }

    @AfterEach
    void tearDown() {
        contaCorrenteRepository.deleteAll();
        agenciaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar o extrato mensal quando dados válidos são fornecidos")
    void quandoExtratoMensalValido() throws Exception {

        // Arrange
        List<Transacao> transacoes = Arrays.asList(transacao1, transacao2);
        given(transacaoService.obterExtratoMensal(contaCorrente.getId(), mesAno)).willReturn(transacoes);

        // Act
        String responseJsonString = mockMvc.perform(get("/transacoes/extrato-mensal")
                        .param("idConta", contaCorrente.getId().toString())
                        .param("mesAno", mesAno.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TransferenciaRequestDTO> resultado = Arrays.asList(objectMapper.readValue(responseJsonString, TransferenciaRequestDTO[].class));

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(t -> t.getValor().equals(transacao1.getValor())));
        assertTrue(resultado.stream().anyMatch(t -> t.getValor().equals(transacao2.getValor())));
    }


    @Test
    @DisplayName("Quando buscamos o extrato anual com dados válidos")
    void quandoExtratoAnualValido() throws Exception {
        // Arrange
        int ano = YearMonth.now().getYear();
        List<Transacao> transacoes = Arrays.asList(transacao1, transacao2);
        when(transacaoService.obterExtratoAnual(anyLong(), anyInt())).thenReturn(transacoes);

        // Act
        String responseJsonString = mockMvc.perform(get("/transacoes/extrato-anual")
                        .param("idConta", "1")
                        .param("ano", String.valueOf(ano))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TransferenciaRequestDTO> resultado = Arrays.asList(objectMapper.readValue(responseJsonString, TransferenciaRequestDTO[].class));

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(t -> t.getValor().equals(transacao1.getValor())));
        assertTrue(resultado.stream().anyMatch(t -> t.getValor().equals(transacao2.getValor())));
    }


    @Test
    @DisplayName("Quando buscamos o extrato filtrado com dados válidos")
    void quandoExtratoFiltradoValido() throws Exception {
        // Arrange
        LocalDateTime dataInicio = LocalDateTime.now().minusMonths(1);
        LocalDateTime dataFim = LocalDateTime.now();
        List<Transacao> transacoes = Arrays.asList(transacao1, transacao2);
        when(transacaoService.obterExtratoFiltrado(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(transacoes);

        // Act
        String responseJsonString = mockMvc.perform(get("/transacoes/extrato-filtrado")
                        .param("idConta", "1")
                        .param("dataInicio", dataInicio.toString())
                        .param("dataFim", dataFim.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TransferenciaRequestDTO> resultado = Arrays.asList(objectMapper.readValue(responseJsonString, TransferenciaRequestDTO[].class));

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(t -> t.getValor().equals(transacao1.getValor())));
        assertTrue(resultado.stream().anyMatch(t -> t.getValor().equals(transacao2.getValor())));
    }
}