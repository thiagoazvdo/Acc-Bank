package com.accenture.academico.Acc.Bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TransacaoTest {

    @Test
    void testConstrutorPadrao() {
        Transacao transacao = new Transacao();
        assertNull(transacao.getId());
        assertNull(transacao.getTipo());
        assertNull(transacao.getValor());
        assertNull(transacao.getDataHora());
        assertNull(transacao.getDescricao());
        assertNull(transacao.getContaCorrente());
        assertNull(transacao.getContaCorrenteRelacionada());
    }

    @Test
    void testGettersAndSetters() {
        Transacao transacao = new Transacao();
        ContaCorrente conta = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
        ContaCorrente contaRelacionada = new ContaCorrente(2L, "10002", BigDecimal.ZERO, null, null, null);

        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor(BigDecimal.valueOf(100.00));
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao("Deposito inicial");
        transacao.setContaCorrente(conta);
        transacao.setContaCorrenteRelacionada(contaRelacionada);

        assertEquals(1L, transacao.getId());
        assertEquals(TipoTransacao.DEPOSITO, transacao.getTipo());
        assertEquals(BigDecimal.valueOf(100.00), transacao.getValor());
        assertNotNull(transacao.getDataHora());
        assertEquals("Deposito inicial", transacao.getDescricao());
        assertEquals(conta, transacao.getContaCorrente());
        assertEquals(contaRelacionada, transacao.getContaCorrenteRelacionada());
    }

    @Test
    void testEqualsAndHashCode() {
        // Teste básico de igualdade
        Transacao transacao1 = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
        Transacao transacao2 = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
        assertEquals(transacao1, transacao2);
        assertEquals(transacao1.hashCode(), transacao2.hashCode());
        

        // Teste de desigualdade com IDs diferentes
        Transacao transacao3 = new Transacao(2L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
        assertNotEquals(transacao1, transacao3);
        assertNotEquals(transacao1.hashCode(), transacao3.hashCode());
        
        // Teste de igualdade com a própria instância
        assertEquals(transacao1, transacao1);
        
        // Teste de desigualdade com classe diferente
        assertNotEquals(transacao1, new Cliente());
    }
    
    @Test
    void testEqualsNulo() {
        // Teste de igualdade quando ambos os IDs são null
    	Transacao transacao1 = new Transacao(null, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
        Transacao transacao2 = new Transacao(null, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
        assertEquals(transacao1, transacao2);

        // Teste de desigualdade quando um ID é null e o outro não
        Transacao transacao3 = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
        assertNotEquals(transacao1, transacao3);
    }
    
    @Test
	void testEqualsWithDifferentCanEqual() {
    	Transacao transacao1 = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null);
	    
	    // Criar uma subclasse de Agencia que retorna false para canEqual
    	Transacao transacaoSubclass = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), "Saque automático", null, null) {
	        @Override
	        public boolean canEqual(Object other) {
	            return false;
	        }
	    };
	    
	    // O equals deve retornar false quando canEqual retornar false
	    assertNotEquals(transacao1, transacaoSubclass);
	}
    
    @Test
    void testHashCodeNulo() {
        // Teste de hashCode com valores nulos
        Transacao transacao1 = new Transacao();
        Transacao transacao2 = new Transacao();
        assertEquals(transacao1.hashCode(), transacao2.hashCode());
    }
}