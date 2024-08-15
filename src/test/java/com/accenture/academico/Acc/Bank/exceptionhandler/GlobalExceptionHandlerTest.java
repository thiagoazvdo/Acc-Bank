package com.accenture.academico.Acc.Bank.exceptionhandler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.CannotCreateTransactionException;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.accenture.academico.Acc.Bank.service.ContaCorrenteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do controlador de Exceções")
class GlobalExceptionHandlerTest {
	
	@Autowired
	ObjectMapper objectMapper;
	 
	@Nested
	@DisplayName("Casos de teste da exception HttpMessageNotReadableException")
	class TestesHandleHttpMessageNotReadableException {
		  
		@Autowired
		private MockMvc mockMvc;
		
		@Test
		@DisplayName("Quando criamos um novo cliente passando uma string em ID Agencia")
		void quandoCriarClienteComIDAgenciaString() throws Exception {
			// Arrange
			String clienteJson = 
					"""
	                  {
	                      "nome": "Biu Lopes",
	                      "cpf": "555.654.777-45",
	                      "telefone": "83998372543",
	                      "email": "biu@email.com",
	                      "idAgencia": "string"
	                  }
	      			""";
	      	
	        // Act
			String responseJsonString = mockMvc.perform(post("/clientes")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(clienteJson))
	                .andExpect(status().isBadRequest())
	                .andDo(print())
	                .andReturn().getResponse().getContentAsString();
	          
			ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
	          
	        // Assert
			assertAll(
					() -> assertEquals("O corpo da requisição não pode ser lido.", resultado.getMensagem()),
					() -> assertEquals(1, resultado.getErros().size()),
					() -> assertTrue(resultado.getErros().contains("JSON parse error: Cannot deserialize value of type `java.lang.Long` from String \"string\": not a valid `java.lang.Long` value"))
			);
		}
	}
	
	@Nested
	@DisplayName("Casos de teste da exception MethodArgumentTypeMismatchException")
	class TestesHandleMethodArgumentTypeMismatchException {
		
		@Autowired
		private MockMvc mockMvc;
		
		@Test
		@DisplayName("Quando buscamos um cliente /clientes/{id} passando uma string em {id}")
		void quandoBuscamosClienteComStringEmID() throws Exception {
			// Arrange
			String idCliente = "idString";
	      	
	        // Act
			String responseJsonString = mockMvc.perform(get("/clientes/" + idCliente)
	                        .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isBadRequest())
	                .andDo(print())
	                .andReturn().getResponse().getContentAsString();
	          
			ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
	          
	        // Assert
			assertAll(
					() -> assertEquals("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"idString\"", resultado.getMensagem()),
					() -> assertEquals(1, resultado.getErros().size()),
					() -> assertTrue(resultado.getErros().contains("id deve ser do tipo java.lang.Long"))
			);
		}
	}
	
	@Nested
	@DisplayName("Casos de teste para falhas na Conexão com o Banco de Dados")
	class TestesHandleConexaoBancoDadosException {
		
		@MockBean
		private AgenciaRepository agenciaRepository;
		
		@Autowired 
		private MockMvc mockMvc;
		
		private AgenciaRequestDTO agenciaRequestDTO;
		
		@BeforeEach
	    void setUp() {
	        agenciaRequestDTO = new AgenciaRequestDTO("Banco do Brasil UFCG", "UFCG", "83933332222");
	    }
		
		@Test
        @DisplayName("Quando criamos uma agencia válida com o Banco de Dados fora do ar")
        void quandoCriarAgenciaValidaBancoDeDadosOff() throws Exception {
            // Arrange
			when(agenciaRepository.save(any(Agencia.class))).thenThrow(CannotCreateTransactionException.class);
			
            // Act
            String responseJsonString = mockMvc.perform(post("/agencias")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agenciaRequestDTO)))
                    .andExpect(status().isServiceUnavailable())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Falha na conexão com o banco de dados. Tente novamente mais tarde.", resultado.getMensagem())
            );
        }
		
		@Test
        @DisplayName("Quando buscamos todas as agências com o Banco de Dados fora do ar")
        void quandoBuscatTodasAgenciasBancoDeDadosOff() throws Exception {
            // Arrange
			when(agenciaRepository.findAll()).thenThrow(DataAccessResourceFailureException.class);
			
            // Act
            String responseJsonString = mockMvc.perform(get("/agencias")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isServiceUnavailable())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Falha na conexão com o banco de dados. Tente novamente mais tarde.", resultado.getMensagem())
            );
        }
	}
	
	@Nested
	@DisplayName("Casos de teste para falhas na todas as")
	class TestesHandleAllException {
		
		@MockBean
		private ContaCorrenteService contaCorrenteService;
		
		@Autowired 
		private MockMvc mockMvc;
		
		@Test
        @DisplayName("Quando uma Exception geral é lançada")
        void quandoExceptionGeralEhLancada() throws Exception {
            // Arrange
			SaqueDepositoRequestDTO saqueDTO = new SaqueDepositoRequestDTO(BigDecimal.valueOf(10), "Saque do salário");
			doThrow(new RuntimeException("Falha interna do servidor")).when(contaCorrenteService).sacar(anyLong(), any(SaqueDepositoRequestDTO.class));
			
            // Act
            String responseJsonString = mockMvc.perform(post("/contas-correntes/1/sacar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saqueDTO)))
                    .andExpect(status().isInternalServerError())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            
        	ResponseError resultado = objectMapper.readValue(responseJsonString, ResponseError.class);
            
            // Assert
            assertAll(
                    () -> assertEquals("Falha interna do servidor", resultado.getMensagem())
            );
        }
	}
}
