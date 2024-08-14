package com.accenture.academico.Acc.Bank.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaJaCadastradaException;
import com.accenture.academico.Acc.Bank.handler.ResponseError;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do controlador de Agencias")
class AgenciaControllerTest {

	@Autowired
	private AgenciaRepository agenciaRepository;

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	Agencia agencia;
	AgenciaRequestDTO agenciaRequestDTO;
	
    @BeforeEach
    void setUp() {
    	agencia = new Agencia(null, "Agencia 1", "Endereco 1", "83911112222", null, null);
    	agenciaRequestDTO = new AgenciaRequestDTO(agencia.getNome(), agencia.getEndereco(), agencia.getTelefone());
    	
    	agencia = agenciaRepository.save(agencia);
    }
    
    @AfterEach
    void tearDown() {
    	agenciaRepository.deleteAll();
    }
	
    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Criar")
    class AgenciaFluxosBasicosCriar{
    	
    	@Test
        @DisplayName("Quando criamos uma nova agencia com dados válidos")
        void quandoCriarAgenciaValida() throws Exception {
            // Arrange
    		agenciaRequestDTO.setTelefone("11944447777");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
            			.contentType(MediaType.APPLICATION_JSON)
            			.content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            Agencia resultado = objectMapper.readValue(responseJsonString, Agencia.class);
            
            // Assert
            assertNotNull(resultado.getId());
            assertEquals(agenciaRequestDTO.getNome(), resultado.getNome());
            assertEquals(agenciaRequestDTO.getEndereco(), resultado.getEndereco());
            assertEquals(agenciaRequestDTO.getTelefone(), resultado.getTelefone());
            assertEquals(resultado.getDataCriacao(), resultado.getDataAtualizacao());
            assertNotNull(resultado.getDataCriacao());
            assertNotNull(resultado.getDataAtualizacao());
        }
    	
        @Test
        @DisplayName("Quando criamos uma agencia com telefone ja cadastrado")
        void quandoCriarAgenciaComTelefoneJaCadastrado() throws Exception {
            // Arrange
        	
            // Act
        	String responseJsonString = mockMvc.perform(post("/agencias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isConflict())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            AgenciaJaCadastradaException exception = new AgenciaJaCadastradaException("telefone", agencia.getTelefone());
        	
            // Assert
            assertEquals(exception.getMessage(), resultado.getMessage());
        }
        
        @Test
        @DisplayName("Quando criamos uma nova agencia com campos nulos")
        void quandoCriarAgenciaCamposNulos() throws Exception {
            // Arrange
        	agenciaRequestDTO.setNome(null);
        	agenciaRequestDTO.setEndereco(null);
        	agenciaRequestDTO.setTelefone(null);
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
            			.contentType(MediaType.APPLICATION_JSON)
            			.content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(3, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Nome da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Endereco da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Telefone da agencia nao pode estar em branco."))
            );

        }
        
        @Test
        @DisplayName("Quando criamos uma nova agencia com campos vazios")
        void quandoCriarAgenciaCamposVazios() throws Exception {
            // Arrange
        	agenciaRequestDTO.setNome("");
        	agenciaRequestDTO.setEndereco("");
        	agenciaRequestDTO.setTelefone("");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
            			.contentType(MediaType.APPLICATION_JSON)
            			.content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(3, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Nome da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Endereco da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Telefone da agencia nao pode estar em branco."))
            );
        }
        
        @Test
        @DisplayName("Quando criamos uma agencia com telefone com letras")
        void quandoCriarAgenciaTelefoneComLetras() throws Exception {
            // Arrange
        	agenciaRequestDTO.setTelefone("telefone123");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos."))
            );
        }
        
        @Test
        @DisplayName("Quando criamos uma agencia com telefone com 12 digitos")
        void quandoCriarAgenciaTelefoneCom12Digitos() throws Exception {
            // Arrange
        	agenciaRequestDTO.setTelefone("000222444666");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos."))
            );
        }
        
        @Test
        @DisplayName("Quando criamos uma agencia com telefone com 10 digitos")
        void quandoCriarAgenciaTelefoneCom10Digitos() throws Exception {
            // Arrange
        	agenciaRequestDTO.setTelefone("0002224446");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos."))
            );
        }
    }
    
    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Atualizar")
    class AgenciaFluxosBasicosAtualizar{
    	
    	@Test
        @DisplayName("Quando atualizamos uma agencia com dados válidos")
        void quandoAtualizamosAgenciaValida() throws Exception {
            // Arrange
    		agenciaRequestDTO.setNome("Nome Novo");
    		agenciaRequestDTO.setEndereco("Endereco Novo");
    		agenciaRequestDTO.setTelefone("85912345678");
    		
            // Act
            String responseJsonString = mockMvc.perform(put("/agencias/" + agencia.getId())
            			.contentType(MediaType.APPLICATION_JSON)
            			.content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            Agencia resultado = objectMapper.readValue(responseJsonString, Agencia.class);
            
            // Assert
            assertNotNull(resultado.getId());
            assertEquals(agenciaRequestDTO.getNome(), resultado.getNome());
            assertEquals(agenciaRequestDTO.getEndereco(), resultado.getEndereco());
            assertEquals(agenciaRequestDTO.getTelefone(), resultado.getTelefone());
        }
        
        @Test
        @DisplayName("Quando atualizamos uma agencia com campos nulos")
        void quandoAtualizamosAgenciaCamposNulos() throws Exception {
            // Arrange
        	agenciaRequestDTO.setNome(null);
        	agenciaRequestDTO.setEndereco(null);
        	agenciaRequestDTO.setTelefone(null);
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/agencias/" + agencia.getId())
            			.contentType(MediaType.APPLICATION_JSON)
            			.content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(3, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Nome da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Endereco da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Telefone da agencia nao pode estar em branco."))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos uma agencia com campos vazios")
        void quandoAtualizamosAgenciaCamposVazios() throws Exception {
            // Arrange
        	agenciaRequestDTO.setNome("");
        	agenciaRequestDTO.setEndereco("");
        	agenciaRequestDTO.setTelefone("");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/agencias/" + agencia.getId())
            			.contentType(MediaType.APPLICATION_JSON)
            			.content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(3, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Nome da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Endereco da agencia nao pode estar em branco.")),
                    () -> assertTrue(resultado.getErrors().contains("Telefone da agencia nao pode estar em branco."))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos uma agencia com telefone com letras")
        void quandoAtualizamosAgenciaTelefoneComLetras() throws Exception {
            // Arrange
        	agenciaRequestDTO.setTelefone("telefone123");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/agencias/" + agencia.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos."))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos uma agencia com telefone com 12 digitos")
        void quandoAtualizamosAgenciaTelefoneCom12Digitos() throws Exception {
            // Arrange
        	agenciaRequestDTO.setTelefone("000222444666");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/agencias/" + agencia.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos."))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos uma agencia com telefone com 10 digitos")
        void quandoAtualizamosAgenciaTelefoneCom10Digitos() throws Exception {
            // Arrange
        	agenciaRequestDTO.setTelefone("0002224446");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/agencias/" + agencia.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos."))
            );
        }
    }
    
    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Buscar")
    class AgenciaFluxosBasicosBuscar{
    	
    	@Test
        @DisplayName("Quando buscamos uma agencia salva pelo id")
        void quandoBuscamosAgenciaValida() throws Exception {
        	// Arrange
        	
            // Act
            String responseJsonString = mockMvc.perform(get("/agencias/" + agencia.getId())
            			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            Agencia resultado = objectMapper.readValue(responseJsonString, Agencia.class);
            
            // Assert
            assertEquals(agencia.getId(), resultado.getId());
            assertEquals(agencia.getNome(), resultado.getNome());
            assertEquals(agencia.getEndereco(), resultado.getEndereco());
            assertEquals(agencia.getTelefone(), resultado.getTelefone());
        }
    	
    	@Test
        @DisplayName("Quando buscamos uma agencia inexistente pelo id")
        void quandoBuscamosAgenciaInexistente() throws Exception {
        	// Arrange
        	Long idInexistente = 999L;
    		
            // Act
            String responseJsonString = mockMvc.perform(get("/agencias/" + idInexistente)
            			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertEquals(String.format("Nao existe uma agencia cadastrada com o id %d", idInexistente) , resultado.getMessage());
        }
    }
    
    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Remover")
    class AgenciaFluxosBasicosRemover{
    	
    	@Test
        @DisplayName("Quando removemos uma agencia pelo id")
        void quandoRemovemosAgenciaValida() throws Exception {
        	// Arrange
        	
            // Act
            String responseJsonString = mockMvc.perform(delete("/agencias/" + agencia.getId())
            			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            // Assert
            assertTrue(responseJsonString.isBlank());
        }
    	
    	@Test
        @DisplayName("Quando removemos uma agencia inexistente")
        void quandoRemovemosAgenciaInexistente() throws Exception {
        	// Arrange
        	Long idInexistente = 999L;
    		
            // Act
            String responseJsonString = mockMvc.perform(delete("/agencias/" + idInexistente)
            			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertEquals(String.format("Nao existe uma agencia cadastrada com o id %d", idInexistente) , resultado.getMessage());
        }
    }
    
    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Listar")
    class AgenciaFluxosBasicosListar{
    	
    	@Test
        @DisplayName("Quando listamos todas agencias salvas")
        void quandoListamosTodasAgencias() throws Exception {
            // Arrange
            Agencia agencia2 = agenciaRepository.save(new Agencia(null, "Agencia 2", "Endereco 2", "987654321", null, null));
            
            // Act
            String responseJsonString = mockMvc.perform(get("/agencias"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            List<Agencia> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Agencia>>() {});
            
            // Assert
            assertEquals(2, resultado.size());
            assertEquals(agencia.getId(), resultado.get(0).getId());
            assertEquals(agencia.getNome(), resultado.get(0).getNome());
            assertEquals(agencia.getEndereco(), resultado.get(0).getEndereco());
            assertEquals(agencia.getTelefone(), resultado.get(0).getTelefone());
            assertEquals(resultado.get(0).getDataCriacao(), resultado.get(0).getDataAtualizacao());
            assertNotNull(resultado.get(0).getDataCriacao());
            assertNotNull(resultado.get(0).getDataAtualizacao());
            
            assertEquals(agencia2.getId(), resultado.get(1).getId());
            assertEquals(agencia2.getNome(), resultado.get(1).getNome());
            assertEquals(agencia2.getEndereco(), resultado.get(1).getEndereco());
            assertEquals(agencia2.getTelefone(), resultado.get(1).getTelefone());
            assertEquals(resultado.get(1).getDataCriacao(), resultado.get(1).getDataAtualizacao());
            assertNotNull(resultado.get(1).getDataCriacao());
            assertNotNull(resultado.get(1).getDataAtualizacao());
        }
    }
}
