package com.accenture.academico.Acc.Bank.service;

import java.util.List;

import com.accenture.academico.Acc.Bank.exception.ConexaoBancoDadosException;
import com.accenture.academico.Acc.Bank.exception.EntidadeEmUsoException;

import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.dto.ClienteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;

import javax.xml.crypto.Data;

@Service
public class ClienteService {

    private static final String MSG_CLIENTE_COM_CONTA_ATIVA = "Cliente de código %d não pode ser removido, " + "pois possui conta ativa";

    private static final String MSG_CONEXAO_BD_PERDIDA = "Falha na conexão com o banco de dados. Tente novamente mais tarde.";

    @Autowired
    private ModelMapper modelMapper;
	
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    public Cliente buscarCliente(Long clienteId) {

        try{
            return clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    private boolean cpfJaCadastrado(String cpf) {
        return clienteRepository.findByCpf(cpf).isPresent();
    }

    public Cliente atualizar(Long clienteId, ClienteRequestDTO clienteRequestDTO){
        Cliente cliente = buscarCliente(clienteId);
        Cliente clienteAtualizado = converterParaCliente(clienteRequestDTO);
        clienteAtualizado.setId(cliente.getId());

        try {
            return clienteRepository.save(clienteAtualizado);
        } catch (EmptyResultDataAccessException e) {
            throw new ClienteNaoEncontradoException(clienteId);
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public Cliente criarCliente(ClienteRequestDTO clienteRequestDTO) {

        try {
            if (cpfJaCadastrado(clienteRequestDTO.getCpf())) throw new ClienteJaCadastradoException(clienteRequestDTO.getCpf());
            Cliente novoCliente = converterParaCliente(clienteRequestDTO);
            novoCliente.setIdAgencia(clienteRequestDTO.getIdAgencia());
            return clienteRepository.save(novoCliente);
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public void removerCliente(Long id) {
        try {
            clienteRepository.delete(buscarCliente(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ClienteNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_CLIENTE_COM_CONTA_ATIVA, id));
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public List<Cliente> listarClientes(){
        try {
            return clienteRepository.findAll();
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    private Cliente converterParaCliente(ClienteRequestDTO clienteDTO) {
        try {
            Long idAgenciaDTO = clienteDTO.getIdAgencia();
            boolean agenciaExiste = agenciaRepository.existsById(idAgenciaDTO);
            if (!agenciaExiste) throw new AgenciaNaoEncontradaException(idAgenciaDTO);
            return modelMapper.map(clienteDTO, Cliente.class);
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }
}
