package com.accenture.academico.Acc.Bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.TipoTransacao;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import com.accenture.academico.Acc.Bank.repository.ContaCorrenteRepository;
import com.accenture.academico.Acc.Bank.repository.TransacaoRepository;
import com.accenture.academico.Acc.Bank.service.ClienteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do controller de Transações")
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;
    
    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transacao transacao1;
    private Transacao transacao2;
    private Transacao transacao3;
    private Transacao transacao4;
    private Transacao transacao5;
    private Transacao transacao6;
    private ContaCorrente conta;

    @BeforeEach
    void setUp() {
        Agencia agencia1 = agenciaRepository.save(new Agencia(null, "Agencia 1", "Endereco 1", "123456789", null, null));
        
        ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Raphael Agra", "111.222.333-45", "83988129070", "raphael@email.com", agencia1.getId());
        Cliente cliente = clienteService.criarCliente(clienteRequestDTO);

        conta = cliente.getContaCorrente();
        
        transacao1 = transacaoRepository.save(new Transacao(null, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100.00), null, "Deposito Inicial", conta, null));
        transacao2 = transacaoRepository.save(new Transacao(null, TipoTransacao.SAQUE, BigDecimal.valueOf(5.00), null, "Saque 1", conta, null));
        transacao3 = transacaoRepository.save(new Transacao(null, TipoTransacao.DEPOSITO, BigDecimal.valueOf(6.00), null, "Deposito 2", conta, null));
        transacao4 = transacaoRepository.save(new Transacao(null, TipoTransacao.SAQUE, BigDecimal.valueOf(7.00), null, "Saque 2", conta, null));
        transacao5 = transacaoRepository.save(new Transacao(null, TipoTransacao.DEPOSITO, BigDecimal.valueOf(8.00), null, "Deposito 3", conta, null));
        transacao6 = transacaoRepository.save(new Transacao(null, TipoTransacao.SAQUE, BigDecimal.valueOf(9.00), null, "Saque 3", conta, null));

        transacao1.setDataHora(LocalDateTime.of(2024, 01, 31, 0, 0));
        transacao2.setDataHora(LocalDateTime.of(2023, 02, 05, 0, 0));
        transacao3.setDataHora(LocalDateTime.of(2023, 02, 15, 0, 0));
        transacao4.setDataHora(LocalDateTime.of(2023, 03, 10, 0, 0));
        transacao5.setDataHora(LocalDateTime.of(2023, 03, 20, 0, 0));
        transacao6.setDataHora(LocalDateTime.of(2023, 03, 30, 0, 0));
        
        transacaoRepository.saveAll(List.of(transacao1, transacao2, transacao3, transacao4, transacao5, transacao6));
    }

    @AfterEach
    void tearDown() {
    	transacaoRepository.deleteAll();
    	contaCorrenteRepository.deleteAll();
        clienteRepository.deleteAll();
        agenciaRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Quando buscamos o extrato geral com dados válidos")
    void quandoExtratoGeralValido() throws Exception {
        // Arrange

        // Act
        String responseJsonString = mockMvc.perform(get("/contas-correntes/" + conta.getId() + "/extrato")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Transacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Transacao>>() {});

        // Assert
        assertEquals(6, resultado.size());
        assertEquals(List.of(transacao1, transacao2, transacao3, transacao4, transacao5, transacao6), resultado);
    }

    @Test
    @DisplayName("Deve retornar o extrato mensal quando dados válidos são fornecidos")
    void quandoExtratoMensalValido() throws Exception {
        // Arrange
    	String mesAno = "03/2023";
    	
        // Act
        String responseJsonString = mockMvc.perform(get("/contas-correntes/" + conta.getId() + "/extrato-mensal")
                        .param("mesAno", mesAno.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Transacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Transacao>>() {});

        // Assert
        assertEquals(3, resultado.size());
        assertEquals(List.of(transacao4, transacao5, transacao6), resultado);
    }


    @Test
    @DisplayName("Quando buscamos o extrato anual com dados válidos")
    void quandoExtratoAnualValido() throws Exception {
        // Arrange
        int ano = 2023;

        // Act
        String responseJsonString = mockMvc.perform(get("/contas-correntes/" + conta.getId() + "/extrato-anual")
                        .param("ano", String.valueOf(ano))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Transacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Transacao>>() {});

        // Assert
        assertEquals(5, resultado.size());
        assertEquals(List.of(transacao2, transacao3, transacao4, transacao5, transacao6), resultado);
    }


    @Test
    @DisplayName("Quando buscamos o extrato filtrado com dados válidos")
    void quandoExtratoFiltradoValido() throws Exception {
        // Arrange
        String dataInicio = "04/02/2023 00:00";
        String dataFim = "21/03/2023 00:00";

        // Act
        String responseJsonString = mockMvc.perform(get("/contas-correntes/" + conta.getId() + "/extrato-filtrado")
                        .param("dataInicio", dataInicio)
                        .param("dataFim", dataFim)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Transacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Transacao>>() {});

        // Assert
        assertEquals(4, resultado.size());
        assertEquals(List.of(transacao2, transacao3, transacao4, transacao5), resultado);
    }
}