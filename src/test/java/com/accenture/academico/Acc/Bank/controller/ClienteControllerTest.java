package com.accenture.academico.Acc.Bank.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.accenture.academico.Acc.Bank.handler.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do controlador de Clientes")
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        clienteRequestDTO = new ClienteRequestDTO("Cliente 1", "88616355491", "988129070", agencia.getId());
        clienteRepository.save(cliente);
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
            assertEquals(resultado.getDataCriacao(), resultado.getDataAtualizacao());
            assertNotNull(resultado.getDataCriacao());
            assertNotNull(resultado.getDataAtualizacao());
        }
        
        @Test
        @DisplayName("Quando criamos um cliente ja cadastrado")
        void quandoCriarClienteJaCadastrado() throws Exception {
            // Arrange
        	
            // Act
        	String responseJsonString = mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isConflict())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
        	
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            ClienteJaCadastradoException exception = new ClienteJaCadastradoException(cliente.getCpf());
        	
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
                    () -> assertEquals(3, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio"))
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
                    () -> assertEquals(4, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
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
            clienteRequestDTO.setTelefone("988129070");
            
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
                    () -> assertEquals(4, resultado.getErrors().size()),
                    () -> assertTrue(resultado.getErrors().contains("Campo nome obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo cpf obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Campo telefone obrigatorio")),
                    () -> assertTrue(resultado.getErrors().contains("Cpf deve ter exatamente 11 digitos numericos"))
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

            Cliente cliente2 = clienteRepository.save(new Cliente(null, "Cliente 2", "12345678810", "987654321", null, null, null, agencia));


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

            assertEquals(cliente2.getId(), resultado.get(1).getId());
            assertEquals(cliente2.getNome(), resultado.get(1).getNome());
            assertEquals(cliente2.getCpf(), resultado.get(1).getCpf());
            assertEquals(cliente2.getTelefone(), resultado.get(1).getTelefone());
        }

    }
}