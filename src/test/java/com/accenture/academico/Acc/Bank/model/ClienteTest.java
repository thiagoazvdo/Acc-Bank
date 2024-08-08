package com.accenture.academico.Acc.Bank.model;

import static org.junit.jupiter.api.Assertions.*;

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
		assertNull(clienteNovo.getContaCorrente());
	}

	@Test
	void testGettersAndSetters() {
		Cliente clienteNovo = new Cliente();
		clienteNovo.setId(2L);
		clienteNovo.setNome("João Silva");
		clienteNovo.setCpf("12345678901");
		clienteNovo.setTelefone("1111-8888");

		assertEquals(2L, clienteNovo.getId());
		assertEquals("João Silva", clienteNovo.getNome());
		assertEquals("12345678901", clienteNovo.getCpf());
		assertEquals("1111-8888", clienteNovo.getTelefone());
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
