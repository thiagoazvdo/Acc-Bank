package com.accenture.academico.Acc.Bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da classe Agencia")
class AgenciaTest {
	
	@Test
	void testConstrutorPadrao() {
		Agencia agenciaNova = new Agencia();
	    assertNull(agenciaNova.getId());
	    assertNull(agenciaNova.getNome());
	    assertNull(agenciaNova.getEndereco());
	    assertNull(agenciaNova.getTelefone());
	    assertNull(agenciaNova.getDataCriacao());
	    assertNull(agenciaNova.getDataAtualizacao());
	}

	@Test
	void testGettersAndSetters() {
		LocalDateTime data = LocalDateTime.of(2023, 01, 31, 0, 0);
		
		Agencia agenciaNova = new Agencia();
		agenciaNova.setId(2L);
		agenciaNova.setNome("Itau Liberdade");
		agenciaNova.setEndereco("Rua da Liberdade");
		agenciaNova.setTelefone("1111-8888");
		agenciaNova.setDataCriacao(data);
		agenciaNova.setDataAtualizacao(data);
		
		assertEquals(2L, agenciaNova.getId());
		assertEquals("Itau Liberdade", agenciaNova.getNome());
		assertEquals("Rua da Liberdade", agenciaNova.getEndereco());
		assertEquals("1111-8888", agenciaNova.getTelefone());
		assertEquals(data, agenciaNova.getDataCriacao());
		assertEquals(data, agenciaNova.getDataAtualizacao());
	}

	@Test
    void testEqualsObject() {
        // Teste básico de igualdade
        Agencia agencia1 = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        Agencia agencia2 = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        assertEquals(agencia1, agencia2);

        // Teste de desigualdade com IDs diferentes
        Agencia agencia3 = new Agencia(2L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        assertNotEquals(agencia1, agencia3);

        // Teste de igualdade com a própria instância
        assertEquals(agencia1, agencia1);

        // Teste de desigualdade com classe diferente
        assertNotEquals(agencia1, new Cliente());
    }
	
	@Test
    void testEqualsNulo() {
        // Teste de igualdade quando ambos os IDs são null
        Agencia agencia1 = new Agencia(null, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        Agencia agencia2 = new Agencia(null, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        assertEquals(agencia1, agencia2);

        // Teste de desigualdade quando um ID é null e o outro não
        Agencia agencia3 = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        assertNotEquals(agencia1, agencia3);
    }
	
	@Test
	void testEqualsWithDifferentCanEqual() {
	    Agencia agencia1 = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
	    
	    // Criar uma subclasse de Agencia que retorna false para canEqual
	    Agencia agenciaSubclass = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null) {
	        @Override
	        public boolean canEqual(Object other) {
	            return false;
	        }
	    };
	    
	    // O equals deve retornar false quando canEqual retornar false
	    assertNotEquals(agencia1, agenciaSubclass);
	}

	
	@Test
    void testHashCode() {
        // Teste de hashCode para objetos iguais
        Agencia agencia1 = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        Agencia agencia2 = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        assertEquals(agencia1.hashCode(), agencia2.hashCode());

        // Teste de hashCode para objetos diferentes
        Agencia agencia3 = new Agencia(2L, "Banco do Brasil UFCG", "UFCG", "3333-2222", null, null);
        assertNotEquals(agencia1.hashCode(), agencia3.hashCode());
    }
	
	@Test
    void testHashCodeNulo() {
        // Teste de hashCode com valores nulos
        Agencia agencia1 = new Agencia();
        Agencia agencia2 = new Agencia();
        assertEquals(agencia1.hashCode(), agencia2.hashCode());
    }

}
