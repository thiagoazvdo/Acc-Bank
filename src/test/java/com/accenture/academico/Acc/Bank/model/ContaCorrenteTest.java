package com.accenture.academico.Acc.Bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaCorrenteTest {

	private ContaCorrente contaCorrente;

	@BeforeEach
	void setUp() {
		Agencia agencia = new Agencia();
		agencia.setId(1l);
    	agencia.setNome("Banco do Brasil UFCG");
    	agencia.setEndereco("UFCG");
    	agencia.setTelefone("3333-2222");
    	
    	Cliente cliente = new Cliente();
    	cliente.setId(1l);
    	cliente.setNome("Raphael Agra");
    	cliente.setCpf("11122233345");
    	cliente.setTelefone("83 8888-8888");
		
		contaCorrente = new ContaCorrente(cliente);
		contaCorrente.setId(1l);
		contaCorrente.setNumero("10001");
	}
	
	@Test
	void testConstrutorPadrao() {
	    ContaCorrente conta = new ContaCorrente();
	    assertNull(conta.getId());
	    assertNull(conta.getNumero());
	    assertNull(conta.getSaldo());
	    assertNull(conta.getDataCriacao());
	    assertNull(conta.getCliente());
	    assertNull(conta.getTransacoes());
	}

	@Test
	void testGettersAndSetters() {
		LocalDateTime data = LocalDateTime.of(2023, 01, 31, 0, 0);
		
		Cliente cliente = new Cliente();
		
		ContaCorrente conta = new ContaCorrente();
		conta.setId(2L);
		conta.setNumero("10002");
		conta.setSaldo(BigDecimal.ZERO);
		conta.setDataCriacao(data);
		conta.setTransacoes(new ArrayList<>());
		conta.setCliente(cliente);
		
		assertEquals(2L, conta.getId());
		assertEquals("10002", conta.getNumero());
		assertEquals(BigDecimal.ZERO, conta.getSaldo());
		assertEquals(data, conta.getDataCriacao());
		assertEquals(new ArrayList<>(), conta.getTransacoes());
		assertEquals(cliente, conta.getCliente());
	}

	@Test
	void testSacar() {
		ContaCorrente conta = new ContaCorrente(2l, "11111", BigDecimal.valueOf(10.0), null, null, null);
		conta.sacar(BigDecimal.valueOf(2.37));

		assertEquals(BigDecimal.valueOf(7.63), conta.getSaldo());
	}

	@Test
	void testSacarComSaldoInsuficiente() {
		BigDecimal saldoAntesDoSaque = contaCorrente.getSaldo();
		
		contaCorrente.sacar(BigDecimal.valueOf(1));
		
		assertEquals(0, contaCorrente.getSaldo().compareTo(saldoAntesDoSaque));
	}

	@Test
	void testSacarZeroReais() {
		BigDecimal saldoAntesDoSaque = contaCorrente.getSaldo();
		contaCorrente.sacar(BigDecimal.valueOf(0));
		
		assertEquals(0, contaCorrente.getSaldo().compareTo(saldoAntesDoSaque));
	}

	@Test
	void testSacarNumeroNegativo() {
		BigDecimal saldoAntesDoSaque = contaCorrente.getSaldo();
		
		contaCorrente.sacar(BigDecimal.valueOf(-1));
		
		assertEquals(0, contaCorrente.getSaldo().compareTo(saldoAntesDoSaque));
	}

	@Test
	void testDepositar() {
		assertEquals(BigDecimal.valueOf(0), contaCorrente.getSaldo());

		contaCorrente.depositar(BigDecimal.valueOf(3.5));
		assertEquals(BigDecimal.valueOf(3.5), contaCorrente.getSaldo());

		contaCorrente.depositar(BigDecimal.valueOf(3.5));
		assertEquals(BigDecimal.valueOf(7.0), contaCorrente.getSaldo());

		contaCorrente.depositar(BigDecimal.valueOf(3.5));
		assertEquals(BigDecimal.valueOf(10.5), contaCorrente.getSaldo());
	}

	@Test
	void testDepositarZeroReais() {
		BigDecimal saldoAntesDoDeposito = contaCorrente.getSaldo();
		contaCorrente.depositar(BigDecimal.valueOf(0));
		assertEquals(0, contaCorrente.getSaldo().compareTo(saldoAntesDoDeposito));
	}

	@Test
	void testDepositarNumeroNegativo() {
		BigDecimal saldoAntesDoDeposito = contaCorrente.getSaldo();
		contaCorrente.depositar(BigDecimal.valueOf(-1));
		assertEquals(0, contaCorrente.getSaldo().compareTo(saldoAntesDoDeposito));
	}

	@Test
	void testAdicionarTransacao() {
		ContaCorrente conta = new ContaCorrente();
		conta.setTransacoes(new ArrayList<Transacao>());

		Transacao transacao = new Transacao(1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(10), LocalDateTime.now(), null, conta, null);
		conta.adicionarTransacao(transacao);

		assertTrue(conta.getTransacoes().contains(transacao));
		assertEquals(1, conta.getTransacoes().size());
		assertEquals(transacao, conta.getTransacoes().get(0));
	}

	@Test
	void testEqualsObject() {
		// Teste básico de igualdade
		ContaCorrente conta2 = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
		assertEquals(contaCorrente, conta2);
		
		// Teste de desigualdade com IDs diferentes
		ContaCorrente conta3 = new ContaCorrente(2L, "10002", BigDecimal.ZERO, null, null, null);
		assertNotEquals(contaCorrente, conta3);
		
		// Teste de igualdade com a própria instância
		assertEquals(contaCorrente, contaCorrente);

		// Teste de desigualdade com classe diferente
		assertNotEquals(contaCorrente, new Cliente());
	}
	
	@Test
    void testEqualsNulo() {
        // Teste de igualdade quando ambos os IDs são null
		ContaCorrente conta1 = new ContaCorrente(null, "10001", BigDecimal.ZERO, null, null, null);
		ContaCorrente conta2 = new ContaCorrente(null, "10001", BigDecimal.ZERO, null, null, null);
        assertEquals(conta1, conta2);

        // Teste de desigualdade quando um ID é null e o outro não
        ContaCorrente conta3 = new ContaCorrente(3L, "10003", BigDecimal.ZERO, null, null, null);
        assertNotEquals(conta1, conta3);
    }
	
	@Test
	void testEqualsWithDifferentCanEqual() {
		ContaCorrente conta1 = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
	    
	    // Criar uma subclasse de ContaCorrente que retorna false para canEqual
		ContaCorrente contaSubclass = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null) {
	        @Override
	        public boolean canEqual(Object other) {
	            return false;
	        }
	    };
	    
	    // O equals deve retornar false quando canEqual retornar false
	    assertNotEquals(conta1, contaSubclass);
	}
	
	@Test
    void testHashCode() {
        // Teste de hashCode para objetos iguais
		ContaCorrente conta1 = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
		ContaCorrente conta2 = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
        assertEquals(conta1.hashCode(), conta2.hashCode());

        // Teste de hashCode para objetos diferentes
        ContaCorrente conta3 = new ContaCorrente(2L, "10002", BigDecimal.ZERO, null, null, null);
        assertNotEquals(conta1.hashCode(), conta3.hashCode());
    }
	
	@Test
    void testHashCodeNulo() {
        // Teste de hashCode com valores nulos
		ContaCorrente conta1 = new ContaCorrente();
		ContaCorrente conta2 = new ContaCorrente();
        assertEquals(conta1.hashCode(), conta2.hashCode());
    }
}
