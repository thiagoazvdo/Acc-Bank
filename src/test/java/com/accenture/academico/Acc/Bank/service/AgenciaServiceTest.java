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

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;

@DisplayName("Testes do service de Agencias")
class AgenciaServiceTest {

    @InjectMocks
    private AgenciaService agenciaService;
	
	@Mock
    private AgenciaRepository agenciaRepository;

    private Agencia agencia;
    private AgenciaRequestDTO agenciaRequestDTO;
	
    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	
        agencia = new Agencia(1L, "Banco do Brasil UFCG", "UFCG", "83933332222", null, null);
        agenciaRequestDTO = new AgenciaRequestDTO("Banco do Brasil UFCG", "UFCG", "83933332222");
    }
    
    @Test
    void testCriarAgencia_Sucesso() {
        // Arrange
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agencia);

        // Act
        Agencia result = agenciaService.criarAgencia(agenciaRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Banco do Brasil UFCG", result.getNome());
        assertEquals("UFCG", result.getEndereco());
        assertEquals("83933332222", result.getTelefone());
        
        verify(agenciaRepository, times(1)).save(any(Agencia.class));
    }
    
    @Test
    void testAtualizarAgencia_Sucesso() {
    	// Arrange
    	Agencia agenciaAtualizada = new Agencia(agencia.getId(), "Banco do Brasil UFPB", "UFPB", "83944445555", null, null);
    	
        when(agenciaRepository.findById(agencia.getId())).thenReturn(Optional.of(agencia));
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agenciaAtualizada);

        // Act
        Agencia result = agenciaService.atualizarAgencia(1L, agenciaRequestDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(agencia, result);
        assertEquals(1L, result.getId());
        assertEquals("Banco do Brasil UFPB", result.getNome());
        assertEquals("UFPB", result.getEndereco());
        assertEquals("83944445555", result.getTelefone());
        
        verify(agenciaRepository, times(1)).findById(agencia.getId());
        verify(agenciaRepository, times(1)).save(agenciaAtualizada);
    }
    
    @Test
    void testAtualizarAgencia_AgenciaNaoEncontrada() {
    	// Arrange
        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AgenciaNaoEncontradaException.class, () -> agenciaService.atualizarAgencia(1L, agenciaRequestDTO));
        verify(agenciaRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarAgencia_Sucesso() {
    	// Arrange
        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));

        // Act
        Agencia result = agenciaService.buscarAgencia(1L);
        
        // Assert
        assertEquals(agencia, result);
    }
    
    @Test
    void testBuscarAgencia_NaoEncontrada() {
    	// Arrange
        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(AgenciaNaoEncontradaException.class, () -> agenciaService.buscarAgencia(1L));
        verify(agenciaRepository, times(1)).findById(1L);
    }
    
    @Test
    void testRemoverAgencia_Sucesso() {
    	// Arrange
        when(agenciaRepository.findById(anyLong())).thenReturn(Optional.of(agencia));
        doNothing().when(agenciaRepository).delete(agencia);

        // Act
        agenciaService.removerAgencia(1L);
        
        // Assert
        verify(agenciaRepository, times(1)).findById(1L);
        verify(agenciaRepository, times(1)).delete(agencia);
    }
    
    @Test
    void testRemoverAgencia_AgenciaNaoEncontrada() {
    	// Arrange
        when(agenciaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AgenciaNaoEncontradaException.class, () -> agenciaService.removerAgencia(1L));
        verify(agenciaRepository, times(1)).findById(1L);
    }
    
    @Test
    void testListarAgencias() {
    	// Arrange
        when(agenciaRepository.findAll()).thenReturn(Collections.singletonList(agencia));
        
        // Act
        List<Agencia> result = agenciaService.listarAgencias();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(agencia, result.get(0));
    }

}
