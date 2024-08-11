package com.accenture.academico.Acc.Bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteComSaldoException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteNaoEncontradaException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.SaldoInsuficienteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.TransferenciaEntreContasIguaisException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.repository.ContaCorrenteRepository;
import com.accenture.academico.Acc.Bank.repository.TransacaoRepository;
class ContaCorrenteServiceTest {

	@InjectMocks
    private ContaCorrenteService contaCorrenteService;

    @Mock
    private ContaCorrenteRepository contaCorrenteRepository;

    @Mock
    private AgenciaService agenciaService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private TransacaoRepository transacaoRepository;
    
    private Agencia agencia;
    private Cliente cliente;
    private ContaCorrente conta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        agencia = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        cliente = new Cliente(1L, "Raphael Agra", "11122233345", "83 8888-8888", null, null, null, agencia);
        
        conta = new ContaCorrente(cliente);
        conta.setId(1L);
    }
    
    @Test
    void testBuscarContaCorrente_Sucesso() {
        // Arrange
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));

        // Act
        ContaCorrente result = contaCorrenteService.buscarContaCorrente(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("10001", result.getNumero());
        assertEquals(BigDecimal.ZERO, result.getSaldo());
        assertEquals(cliente, result.getCliente());
        assertEquals(new ArrayList<>(), result.getTransacoes());
        
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testBuscarContaCorrente_ContaNaoEncontrada() {
        // Arrange
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.buscarContaCorrente(1L));
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testBuscarContaCorrentePorNumero_Sucesso() {
        // Arrange
        when(contaCorrenteRepository.findByNumero("10001")).thenReturn(Optional.of(conta));

        // Act
        ContaCorrente result = contaCorrenteService.buscarContaCorrentePorNumero("10001");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("10001", result.getNumero());
        assertEquals(BigDecimal.ZERO, result.getSaldo());
        assertEquals(cliente, result.getCliente());
        assertEquals(new ArrayList<>(), result.getTransacoes());
        
        verify(contaCorrenteRepository, times(1)).findByNumero("10001");
    }
    
    @Test
    void testBuscarContaCorrentePorNumero_ContaNaoEncontrada() {
        // Arrange
        when(contaCorrenteRepository.findByNumero("10001")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.buscarContaCorrentePorNumero("10001"));
        verify(contaCorrenteRepository, times(1)).findByNumero("10001");
    }
    
    @Test
    void testRemoverContaCorrente_Sucesso() {
        // Arrange
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));
        doNothing().when(contaCorrenteRepository).delete(conta);
        
        // Act
        contaCorrenteService.removerContaCorrente(1L);

        // Assert
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).delete(conta);
    }
    
    @Test
    void testRemoverContaCorrente_ContaNaoEncontrada() {
        // Arrange
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.removerContaCorrente(1L));
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testRemoverContaCorrente_ComSaldo() {
        // Arrange
        conta.setSaldo(new BigDecimal("100.00"));
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));

        // Act & Assert
        assertThrows(ContaCorrenteComSaldoException.class, () -> contaCorrenteService.removerContaCorrente(1L));
    }
    
    @Test
    void testSacar_Sucesso() {
        // Arrange
        conta.setSaldo(BigDecimal.valueOf(200.0));

        SaqueDepositoRequestDTO saqueDTO = new SaqueDepositoRequestDTO();
        saqueDTO.setValor(BigDecimal.valueOf(105.36));
        saqueDTO.setDescricao("descricao para o saque");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));

        // Act
        contaCorrenteService.sacar(1L, saqueDTO);

        // Assert
        assertEquals(BigDecimal.valueOf(94.64), conta.getSaldo());
        
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).save(conta);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }
    
    @Test
    void testSacar_ContaCorrenteNaoEncontrada() {
        // Arrange
        SaqueDepositoRequestDTO saqueDTO = new SaqueDepositoRequestDTO();
        saqueDTO.setValor(BigDecimal.valueOf(106.36));
        saqueDTO.setDescricao("descricao para o saque");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.sacar(1L, saqueDTO));
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testSacar_SaldoInsuficienteException() {
        // Arrange
    	conta.setSaldo(BigDecimal.valueOf(200.0));
    	
        SaqueDepositoRequestDTO saqueDTO = new SaqueDepositoRequestDTO();
        saqueDTO.setValor(BigDecimal.valueOf(201.0));
        saqueDTO.setDescricao("descricao para o saque");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class, () -> contaCorrenteService.sacar(1L, saqueDTO));
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testDepositar_Sucesso() {
        // Arrange
    	conta.setSaldo(BigDecimal.valueOf(5));
    	
        SaqueDepositoRequestDTO depositoDTO = new SaqueDepositoRequestDTO();
        depositoDTO.setValor(BigDecimal.valueOf(105.36));
        depositoDTO.setDescricao("descricao para o deposito");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));

        // Act
        contaCorrenteService.depositar(1L, depositoDTO);

        // Assert
        assertEquals(BigDecimal.valueOf(110.36), conta.getSaldo());
        
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).save(conta);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }
    
    @Test
    void testDepositar_ContaCorrenteNaoEncontrada() {
        // Arrange
        SaqueDepositoRequestDTO depositoDTO = new SaqueDepositoRequestDTO();
        depositoDTO.setValor(BigDecimal.valueOf(106.36));
        depositoDTO.setDescricao("descricao para o deposito");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.depositar(1L, depositoDTO));
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testTransferir_Sucesso() {
        // Arrange
    	conta.setSaldo(BigDecimal.valueOf(200.0));

        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero("10002");
        contaDestino.setSaldo(BigDecimal.valueOf(36.00));

        TransferenciaRequestDTO transferenciaDTO = new TransferenciaRequestDTO();
        transferenciaDTO.setNumeroContaDestino("10002");
        transferenciaDTO.setValor(BigDecimal.valueOf(50.05));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaCorrenteRepository.findByNumero("10002")).thenReturn(Optional.of(contaDestino));

        // Act
        contaCorrenteService.transferir(1L, transferenciaDTO);

        // Assert
        assertEquals(BigDecimal.valueOf(149.95), conta.getSaldo());
        assertEquals(BigDecimal.valueOf(86.05), contaDestino.getSaldo());
        
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).findByNumero("10002");
        
        verify(contaCorrenteRepository, times(1)).save(conta);
        verify(contaCorrenteRepository, times(1)).save(contaDestino);
        verify(transacaoRepository, times(2)).save(any(Transacao.class));
    }
    
    @Test
    void testTransferir_SaldoInsuficienteException() {
        // Arrange
    	conta.setSaldo(BigDecimal.valueOf(200.0));

        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero("10002");
        contaDestino.setSaldo(BigDecimal.valueOf(36.00));

        TransferenciaRequestDTO transferenciaDTO = new TransferenciaRequestDTO();
        transferenciaDTO.setNumeroContaDestino("10002");
        transferenciaDTO.setValor(BigDecimal.valueOf(200.01));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaCorrenteRepository.findByNumero("10002")).thenReturn(Optional.of(contaDestino));

        // Act & Assert
        
        assertThrows(SaldoInsuficienteException.class, () -> contaCorrenteService.transferir(1L, transferenciaDTO));
        assertEquals(BigDecimal.valueOf(200.0), conta.getSaldo());
        assertEquals(BigDecimal.valueOf(36.0), contaDestino.getSaldo());
        
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).findByNumero("10002");
    }
    
    @Test
    void testTransferir_ContaOrigemNaoEncontrada() {
        // Arrange
        TransferenciaRequestDTO transferenciaDTO = new TransferenciaRequestDTO();
        transferenciaDTO.setValor(BigDecimal.valueOf(50.0));
        transferenciaDTO.setNumeroContaDestino("10002");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.transferir(1L, transferenciaDTO));
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testTransferir_ContaDestinoNaoEncontrada() {
        // Arrange
        conta.setSaldo(BigDecimal.valueOf(100.0));
        
        TransferenciaRequestDTO transferenciaDTO = new TransferenciaRequestDTO();
        transferenciaDTO.setValor(BigDecimal.valueOf(50.0));
        transferenciaDTO.setNumeroContaDestino("10002");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaCorrenteRepository.findByNumero("10002")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaCorrenteNaoEncontradaException.class, () -> contaCorrenteService.transferir(1L, transferenciaDTO));
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).findByNumero("10002");
    }
    
    @Test
    void testTransferir_MesmaContaException() {
        // Arrange
        conta.setSaldo(BigDecimal.valueOf(100.0));

        TransferenciaRequestDTO transferenciaDTO = new TransferenciaRequestDTO();
        transferenciaDTO.setValor(BigDecimal.valueOf(50.0));
        transferenciaDTO.setNumeroContaDestino("10001");

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaCorrenteRepository.findByNumero("10001")).thenReturn(Optional.of(conta));

        // Act & Assert
        assertThrows(TransferenciaEntreContasIguaisException.class, () -> contaCorrenteService.transferir(1L, transferenciaDTO));
        verify(contaCorrenteRepository, times(1)).findById(1L);
        verify(contaCorrenteRepository, times(1)).findByNumero("10001");
    }
}
