package com.accenture.academico.Acc.Bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;

@DisplayName("Testes do service de Clientes")
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente(1L, "Cliente 1", "1234567896910", "1111-8888", null, null, null);
        clienteRequestDTO = new ClienteRequestDTO("João da Silva", "12345678900", "5555-5555");
    }

    @Test
    void testCriarCliente_Sucesso() {
        // Arrange
        when(clienteRepository.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente result = clienteService.criarCliente(clienteRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).findByCpf(clienteRequestDTO.getCpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCriarCliente_CpfJaCadastrado() {
        // Arrange
        when(clienteRepository.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.of(cliente));

        // Act & Assert
        assertThrows(ClienteJaCadastradoException.class, () -> clienteService.criarCliente(clienteRequestDTO));
        verify(clienteRepository, times(1)).findByCpf(clienteRequestDTO.getCpf());
    }

    @Test
    void testAtualizarCliente_Sucesso() {
        // Arrange
        Cliente clienteAtualizado = new Cliente(cliente.getId(), "Cliente 1", "88615266473", "98886-7878", null, null, null);

        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // Act
        Cliente result = clienteService.atualizar(1L, clienteRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteAtualizado, result);
        assertEquals(1L, result.getId());
        assertEquals("98886-7878", result.getTelefone());

        verify(clienteRepository, times(1)).findById(cliente.getId());
        verify(clienteRepository, times(1)).save(clienteAtualizado);
    }

    @Test
    void testAtualizarCliente_ClienteNaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.atualizar(1L, clienteRequestDTO));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarCliente_Sucesso() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente result = clienteService.buscarCliente(1L);

        // Assert
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarCliente_NaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.buscarCliente(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testRemoverCliente_Sucesso() {
        // Arrange
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        // Act
        clienteService.removerCliente(1L);

        // Assert
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void testRemoverCliente_ClienteNaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.removerCliente(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testListarClientes() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Collections.singletonList(cliente));

        // Act
        List<Cliente> result = clienteService.listarClientes();

        // Assert
        assertEquals(1, result.size());
        assertEquals(cliente, result.get(0));
    }
}

