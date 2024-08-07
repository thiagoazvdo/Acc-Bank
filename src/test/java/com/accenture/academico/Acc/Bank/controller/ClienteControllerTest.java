package com.accenture.academico.Acc.Bank.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.model.Cliente;
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

    private ObjectMapper objectMapper = new ObjectMapper();
    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Cliente 1", "88616355491", "988129070", null);
        clienteRequestDTO = new ClienteRequestDTO(cliente.getNome(), cliente.getCpf(), cliente.getTelefone());

        clienteRepository.save(cliente);
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Adicionar")
    class ClienteAdicionarFluxosBasicos {

        @Test
        @DisplayName("Quando criamos um novo cliente com dados válidos")
        void quandoCriarClienteValido() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Cliente Novo", "12345678901", "988129070");

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
        }

        @Test
        @DisplayName("Quando criamos um novo cliente com nome nulo")
        void quandoCriarClienteNomeNulo() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO(null, "12345678901", "988129070");

            // Act
            mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Quando criamos um novo cliente com CPF nulo")
        void quandoCriarClienteCpfNulo() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Cliente Sem CPF", null, "988129070");

            // Act
            mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Quando criamos um novo cliente com telefone vazio")
        void quandoCriarClienteTelefoneVazio() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Cliente Sem Telefone", "12345678901", "");

            // Act
            mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Atualizar")
    class ClienteAtualizarFluxosBasicos {

        @Test
        @DisplayName("Quando atualizamos um cliente com nome nulo")
        void quandoAtualizamosClienteNomeNulo() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO(null, "12345678910", "9999-9999");

            // Act
            mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Quando atualizamos um cliente com CPF nulo")
        void quandoAtualizamosClienteCpfNulo() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Nome Novo", null, "9999-9999");

            // Act
            mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Quando atualizamos um cliente com telefone vazio")
        void quandoAtualizamosClienteTelefoneVazio() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Nome Novo", "12345678910", "");

            // Act
            mockMvc.perform(put("/clientes/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Quando tentamos atualizar um cliente que não existe")
        void quandoAtualizamosClienteNaoExistente() throws Exception {
            // Arrange
            ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO("Nome Novo", "12345678910", "9999-9999");
            Long idNaoExistente = 999L;

            // Act
            mockMvc.perform(put("/clientes/" + idNaoExistente)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Buscar")
    class ClienteBuscarFluxosBasicos {

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

            // Assert
            assertEquals(String.format("Nao existe um cliente cadastrado com o id %d", idInexistente) , resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Remover")
    class ClienteRemoverFluxosBasicos {

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

            // Assert
            assertEquals(String.format("Nao existe um cliente cadastrado com o id %d", idInexistente) , resultado.getMessage());
        }

    }

    @Nested
    @DisplayName("Conjunto casos de teste do endpoint Listar")
    class ClienteListarFluxosBasicos {

        @Test
        @DisplayName("Quando listamos todos cliente salvos")
        void quandoListamosTodosClientes() throws Exception {
            // Arrange

            Cliente cliente2 = clienteRepository.save(new Cliente(null, "Cliente 2", "12345678810", "987654321", null));


            // Act
            String responseJsonString = mockMvc.perform(get("/clientes"))
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