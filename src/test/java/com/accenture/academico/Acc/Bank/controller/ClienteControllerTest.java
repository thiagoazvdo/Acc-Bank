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

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.handler.ResponseError;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import com.accenture.academico.Acc.Bank.service.ClienteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do controlador de Clientes")
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    private Cliente cliente;

    private Agencia agencia;
    private ClienteRequestDTO clienteRequestDTO;

    @BeforeEach
    void setUp() {
        agencia = agenciaRepository.save(new Agencia(null, "Agencia 1", "Endereco 1", "123456789", null, null));
        
        clienteRequestDTO = new ClienteRequestDTO("Cliente 1", "11122233345", "83988129070", agencia.getId());
        cliente = clienteService.criarCliente(clienteRequestDTO);

    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        agenciaRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Adicionar")
    class ClienteFluxosBasicosAdicionar {

        @Test
        @DisplayName("Quando criamos um novo cliente com dados validos")
        void quandoCriarClienteValido() throws Exception {
            // Arrange
        	clienteRequestDTO.setNome("Raphael");
        	clienteRequestDTO.setCpf("77788899900");
        	clienteRequestDTO.setTelefone("83911112222");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, Cliente.class);

            // Assert
            assertNotNull(resultado.getId());
            assertEquals(clienteRequestDTO.getNome(), resultado.getNome());
            assertEquals(clienteRequestDTO.getCpf(), resultado.getCpf());
            assertEquals(clienteRequestDTO.getTelefone(), resultado.getTelefone());
            assertNotNull(resultado.getContaCorrente());
            assertNotNull(resultado.getDataCriacao());
            assertNotNull(resultado.getDataAtualizacao());
            assertEquals(resultado.getDataCriacao(), resultado.getDataAtualizacao());
        }
        
        @Test
        @DisplayName("Quando criamos um cliente com cpf ja cadastrado")
        void quandoCriarClienteComCpfJaCadastrado() throws Exception {
            // Arrange
        	
            // Act
        	String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isConflict())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            ClienteJaCadastradoException exception = new ClienteJaCadastradoException("cpf", cliente.getCpf());
        	
            // Assert
            assertEquals(exception.getMessage(), resultado.getMessage());
        }
        
        @Test
        @DisplayName("Quando criamos um cliente com telefone ja cadastrado")
        void quandoCriarClienteComTelefoneJaCadastrado() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("66655544499");
        	
            // Act
        	String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isConflict())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            ClienteJaCadastradoException exception = new ClienteJaCadastradoException("telefone", cliente.getTelefone());
        	
            // Assert
            assertEquals(exception.getMessage(), resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um novo cliente com campos nulos")
        void quandoCriarClienteCamposNulos() throws Exception {
            // Arrange
        	clienteRequestDTO.setNome(null);
        	clienteRequestDTO.setCpf(null);
        	clienteRequestDTO.setTelefone(null);
        	clienteRequestDTO.setIdAgencia(null);
        	
            // Act
        	String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(4, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo idAgencia obrigatorio"))
            );
        }
        
        @Test
        @DisplayName("Quando criamos um novo cliente com campos vazios")
        void quandoCriarClienteCamposVazios() throws Exception {
            // Arrange
        	clienteRequestDTO.setNome("");
        	clienteRequestDTO.setCpf("");
        	clienteRequestDTO.setTelefone("");
        	
            // Act
        	String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(5, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos")),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }

        @Test
        @DisplayName("Quando criamos um novo cliente com CPF com letras")
        void quandoCriarClienteCpfComLetras() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("cpf22233300");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando criamos um novo cliente com CPF com 12 digitos")
        void quandoCriarClienteCpfCom12Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("000222444666");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando criamos um novo cliente com CPF com 10 digitos")
        void quandoCriarClienteCpfCom10Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("0002224446");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando criamos um novo cliente com telefone com letras")
        void quandoCriarClienteTelefoneComLetras() throws Exception {
            // Arrange
        	clienteRequestDTO.setTelefone("telefone123");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando criamos um novo cliente com telefone com 12 digitos")
        void quandoCriarClienteTelefoneCom12Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setTelefone("000222444666");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando criamos um novo cliente com telefone com 10 digitos")
        void quandoCriarClienteTelefoneCom10Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setTelefone("0002224446");
        	
            // Act
            String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Atualizar")
    class ClienteFluxosBasicosAtualizar {
    	
    	@Test
        @DisplayName("Quando atualizamos um cliente com dados válidos")
        void quandoAtualizamosClienteValido() throws Exception {
            // Arrange
            clienteRequestDTO.setNome("Cliente Novo");
            clienteRequestDTO.setCpf("12345678901");
            clienteRequestDTO.setTelefone("83988129070");
            
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, Cliente.class);

            // Assert
            assertEquals(cliente.getId(), resultado.getId());
            assertEquals(clienteRequestDTO.getNome(), resultado.getNome());
            assertEquals(clienteRequestDTO.getCpf(), resultado.getCpf());
            assertEquals(clienteRequestDTO.getTelefone(), resultado.getTelefone());
            assertEquals(cliente.getContaCorrente(), resultado.getContaCorrente());
        }
    	
    	@Test
        @DisplayName("Quando atualizamos um cliente inexistente")
        void quandoAtualizamosClienteInexistente() throws Exception {
            // Arrange
    		Long idInexistente = 999L;
        	
            // Act
        	String responseJsonString = mockMvc.perform(put("/clientes/" + idInexistente)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            ClienteNaoEncontradoException exception = new ClienteNaoEncontradoException(idInexistente);
        	
            // Assert
            assertEquals(exception.getMessage(), resultado.getMessage());
        }

    	@Test
        @DisplayName("Quando atualizamos um cliente com campos nulos")
        void quandoAtualizamosClienteCamposNulos() throws Exception {
            // Arrange
        	clienteRequestDTO.setNome(null);
        	clienteRequestDTO.setCpf(null);
        	clienteRequestDTO.setTelefone(null);
        	
            // Act
        	String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(3, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio"))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos um cliente com campos vazios")
        void quandoAtualizamosClienteCamposVazios() throws Exception {
            // Arrange
        	clienteRequestDTO.setNome("");
        	clienteRequestDTO.setCpf("");
        	clienteRequestDTO.setTelefone("");
        	
            // Act
        	String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(5, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos")),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }

        @Test
        @DisplayName("Quando atualizamos um cliente com CPF com letras")
        void quandoAtualizamosClienteCpfComLetras() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("cpf22233300");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos um cliente com CPF com 12 digitos")
        void quandoAtualizamosClienteCpfCom12Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("000222444666");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos um cliente com CPF com 10 digitos")
        void quandoAtualizamosClienteCpfCom10Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setCpf("0002224446");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos um cliente com telefone com letras")
        void quandoAtualizamosClienteTelefoneComLetras() throws Exception {
            // Arrange
        	clienteRequestDTO.setTelefone("telefone123");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos um cliente com telefone com 12 digitos")
        void quandoAtualizamosClienteTelefoneCom12Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setTelefone("000222444666");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }
        
        @Test
        @DisplayName("Quando atualizamos um cliente com telefone com 10 digitos")
        void quandoAtualizamosClienteTelefoneCom10Digitos() throws Exception {
            // Arrange
        	clienteRequestDTO.setTelefone("0002224446");
        	
            // Act
            String responseJsonString = mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Telefone deve ter exatamente 11 digitos numericos"))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Buscar")
    class ClienteFluxosBasicosBuscar {

    	@Test
        @DisplayName("Quando buscamos um cliente válidos")
        void quandoBuscamosClienteValido() throws Exception {
            // Arrange
            
            // Act
            String responseJsonString = mockMvc.perform(get("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, Cliente.class);

            // Assert
            assertEquals(cliente.getId(), resultado.getId());
            assertEquals(clienteRequestDTO.getNome(), resultado.getNome());
            assertEquals(clienteRequestDTO.getCpf(), resultado.getCpf());
            assertEquals(clienteRequestDTO.getTelefone(), resultado.getTelefone());
        }
    	
        @Test
        @DisplayName("Quando buscamos um cliente inexistente pelo id")
        void quandoBuscamosClienteInexistente() throws Exception {
            // Arrange
            Long idInexistente = 999L;

            // Act
            String responseJsonString = mockMvc.perform(get("/clientes/" + idInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            ClienteNaoEncontradoException exception = new ClienteNaoEncontradoException(idInexistente);
        	
            // Assert
            assertEquals(exception.getMessage(), resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Remover")
    class ClienteFluxosBasicosRemover {

    	@Test
        @DisplayName("Quando removemos um cliente pelo id")
        void quandoRemovemosClienteValido() throws Exception {
        	// Arrange
        	
            // Act
            String responseJsonString = mockMvc.perform(delete("/clientes/" + cliente.getId())
            			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
            
            // Assert
            assertTrue(responseJsonString.isBlank());
        }
    	
        @Test
        @DisplayName("Quando removemos um cliente inexistente")
        void quandoRemovemosClienteInexistente() throws Exception {
            // Arrange
            Long idInexistente = 999L;

            // Act
            String responseJsonString = mockMvc.perform(delete("/clientes/" + idInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            ClienteNaoEncontradoException exception = new ClienteNaoEncontradoException(idInexistente);
        	
            // Assert
            assertEquals(exception.getMessage(), resultado.getMessage());
        }

    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Listar")
    class ClienteFluxosBasicosListar {

        @Test
        @DisplayName("Quando listamos todos cliente salvos")
        void quandoListamosTodosClientes() throws Exception {
            // Arrange
        	ClienteRequestDTO clienteRequestDTO2 = new ClienteRequestDTO("Cliente 2", "12345678810", "83987654321", agencia.getId());
            Cliente cliente2 = clienteService.criarCliente(clienteRequestDTO2);
        	
            // Act
            String responseJsonString = mockMvc.perform(get("/clientes")
            			.contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Cliente> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Cliente>>() {});

            // Assert
            assertEquals(2, resultado.size());
            assertEquals(cliente.getId(), resultado.get(0).getId());
            assertEquals(cliente.getNome(), resultado.get(0).getNome());
            assertEquals(cliente.getCpf(), resultado.get(0).getCpf());
            assertEquals(cliente.getTelefone(), resultado.get(0).getTelefone());
            assertEquals(cliente.getContaCorrente(), resultado.get(0).getContaCorrente());
            

            assertEquals(cliente2.getId(), resultado.get(1).getId());
            assertEquals(cliente2.getNome(), resultado.get(1).getNome());
            assertEquals(cliente2.getCpf(), resultado.get(1).getCpf());
            assertEquals(cliente2.getTelefone(), resultado.get(1).getTelefone());
            assertEquals(cliente2.getContaCorrente(), resultado.get(1).getContaCorrente());
        }

    }
}