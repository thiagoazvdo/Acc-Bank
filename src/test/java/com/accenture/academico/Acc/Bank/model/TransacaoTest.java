package com.accenture.academico.Acc.Bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoTest {

    @Test
    @DisplayName("Testa o construtor padrão")
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
    @DisplayName("Testa os getters e setters")
    void testGettersAndSetters() {
        Transacao transacao = new Transacao();
        ContaCorrente conta = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null);
        ContaCorrente contaRelacionada = new ContaCorrente(2L, "10002", BigDecimal.ZERO, null, null, null);

        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor(BigDecimal.valueOf(100.00));
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao("Depósito inicial");
        transacao.setContaCorrente(conta);
        transacao.setContaCorrenteRelacionada(contaRelacionada);

        assertEquals(1L, transacao.getId());
        assertEquals(TipoTransacao.DEPOSITO, transacao.getTipo());
        assertEquals(BigDecimal.valueOf(100.00), transacao.getValor());
        assertNotNull(transacao.getDataHora());
        assertEquals("Depósito inicial", transacao.getDescricao());
        assertEquals(conta, transacao.getContaCorrente());
        assertEquals(contaRelacionada, transacao.getContaCorrenteRelacionada());
    }

    @Test
    @DisplayName("Testa equals e hashCode")
    void testEqualsAndHashCode() {
        Transacao transacao1 = new Transacao();
        transacao1.setId(1L);
        transacao1.setTipo(TipoTransacao.SAQUE);
        transacao1.setValor(BigDecimal.valueOf(50.00));
        transacao1.setDataHora(LocalDateTime.now());
        transacao1.setDescricao("Saque automático");
        transacao1.setContaCorrente(new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null));
        transacao1.setContaCorrenteRelacionada(new ContaCorrente(2L, "10002", BigDecimal.ZERO, null, null, null));

        Transacao transacao2 = new Transacao();
        transacao2.setId(1L);
        transacao2.setTipo(TipoTransacao.SAQUE);
        transacao2.setValor(BigDecimal.valueOf(50.00));
        transacao2.setDataHora(transacao1.getDataHora());
        transacao2.setDescricao("Saque automático");
        transacao2.setContaCorrente(new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, null, null));
        transacao2.setContaCorrenteRelacionada(new ContaCorrente(2L, "10002", BigDecimal.ZERO, null, null, null));

        assertEquals(transacao1, transacao2);
        assertEquals(transacao1.hashCode(), transacao2.hashCode());

        // Testa objetos diferentes
        Transacao transacao3 = new Transacao();
        transacao3.setId(2L);
        transacao3.setTipo(TipoTransacao.DEPOSITO);
        transacao3.setValor(BigDecimal.valueOf(200.00));
        transacao3.setDataHora(LocalDateTime.now().minusDays(1));
        transacao3.setDescricao("Depósito manual");
        transacao3.setContaCorrente(new ContaCorrente(3L, "10003", BigDecimal.ZERO, null, null, null));
        transacao3.setContaCorrenteRelacionada(new ContaCorrente(4L, "10004", BigDecimal.ZERO, null, null, null));

        assertNotEquals(transacao1, transacao3);
        assertNotEquals(transacao1.hashCode(), transacao3.hashCode());
    }
}