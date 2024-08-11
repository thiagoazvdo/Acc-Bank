package com.accenture.academico.Acc.Bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da classe Cliente")
class ClienteTest {

	@Test
	void testConstrutorPadrao() {
		Cliente clienteNovo = new Cliente();
		assertNull(clienteNovo.getId());
		assertNull(clienteNovo.getNome());
		assertNull(clienteNovo.getCpf());
		assertNull(clienteNovo.getTelefone());
		assertNull(clienteNovo.getDataCriacao());
	    assertNull(clienteNovo.getDataAtualizacao());
		assertNull(clienteNovo.getContaCorrente());
	}

	@Test
	void testGettersAndSetters() {
		LocalDateTime data = LocalDateTime.of(2023, 01, 31, 0, 0);
		
		Cliente clienteNovo = new Cliente();
		ContaCorrente conta = new ContaCorrente(1L, "10001", BigDecimal.ZERO, null, clienteNovo, null);

		clienteNovo.setId(2L);
		clienteNovo.setNome("João Silva");
		clienteNovo.setCpf("12345678901");
		clienteNovo.setTelefone("1111-8888");
		clienteNovo.setDataCriacao(data);
		clienteNovo.setDataAtualizacao(data);
		clienteNovo.setContaCorrente(conta);

		assertEquals(2L, clienteNovo.getId());
		assertEquals("João Silva", clienteNovo.getNome());
		assertEquals("12345678901", clienteNovo.getCpf());
		assertEquals("1111-8888", clienteNovo.getTelefone());
		assertEquals(data, clienteNovo.getDataCriacao());
		assertEquals(data, clienteNovo.getDataAtualizacao());
		assertEquals(conta, clienteNovo.getContaCorrente());
	}

	@Test
	void testEqualsObject() {
		// Teste básico de igualdade
		Cliente cliente1 = new Cliente();
		cliente1.setId(1L);
		cliente1.setNome("João Silva");
		cliente1.setCpf("12345678901");
		cliente1.setTelefone("1111-8888");

		Cliente cliente2 = new Cliente();
		cliente2.setId(1L);
		cliente2.setNome("João Silva");
		cliente2.setCpf("12345678901");
		cliente2.setTelefone("1111-8888");

		assertEquals(cliente1, cliente2);

		// Teste de desigualdade com IDs diferentes
		Cliente cliente3 = new Cliente();
		cliente3.setId(2L);
		cliente3.setNome("João Silva");
		cliente3.setCpf("12345678901");
		cliente3.setTelefone("1111-8888");

		assertNotEquals(cliente1, cliente3);
		
		// Teste de igualdade com a própria instância
        assertEquals(cliente1, cliente1);

        // Teste de desigualdade com classe diferente
        assertNotEquals(cliente1, new ContaCorrente());
	}
	
	@Test
    void testEqualsNulo() {
        // Teste de igualdade quando ambos os IDs são null
		Cliente cliente1 = new Cliente(null, "João Silva", "12345678901", "1111-8888", null, null, null, null);
		Cliente cliente2 = new Cliente(null, "João Silva", "12345678901", "1111-8888", null, null, null, null);
        assertEquals(cliente1, cliente2);

        // Teste de desigualdade quando um ID é null e o outro não
        Cliente cliente3 = new Cliente(1L, "João Silva", "12345678901", "1111-8888", null, null, null, null);
        assertNotEquals(cliente1, cliente3);
    }

	@Test
	void testEqualsWithDifferentCanEqual() {
		Cliente cliente1 = new Cliente();
		cliente1.setId(1L);
		cliente1.setNome("João Silva");
		cliente1.setCpf("12345678901");
		cliente1.setTelefone("1111-8888");

		// Criar uma subclasse de Cliente que retorna false para canEqual
		Cliente clienteSubclass = new Cliente() {
			@Override
			public boolean canEqual(Object other) {
				return false;
			}
		};
		clienteSubclass.setId(1L);
		clienteSubclass.setNome("João Silva");
		clienteSubclass.setCpf("12345678901");
		clienteSubclass.setTelefone("1111-8888");

		// O equals deve retornar false quando canEqual retornar false
		assertNotEquals(cliente1, clienteSubclass);
	}

	@Test
	void testHashCode() {
		// Teste de hashCode para objetos iguais
		Cliente cliente1 = new Cliente();
		cliente1.setId(1L);
		cliente1.setNome("João Silva");
		cliente1.setCpf("12345678901");
		cliente1.setTelefone("1111-8888");

		Cliente cliente2 = new Cliente();
		cliente2.setId(1L);
		cliente2.setNome("João Silva");
		cliente2.setCpf("12345678901");
		cliente2.setTelefone("1111-8888");

		assertEquals(cliente1.hashCode(), cliente2.hashCode());

		// Teste de hashCode para objetos diferentes
		Cliente cliente3 = new Cliente();
		cliente3.setId(2L);
		cliente3.setNome("João Silva");
		cliente3.setCpf("12345678901");
		cliente3.setTelefone("1111-8888");

		assertNotEquals(cliente1.hashCode(), cliente3.hashCode());
	}

	@Test
	void testHashCodeNulo() {
		// Teste de hashCode com valores nulos
		Cliente cliente1 = new Cliente();
		Cliente cliente2 = new Cliente();
		assertEquals(cliente1.hashCode(), cliente2.hashCode());
	}
}
