package com.accenture.academico.Acc.Bank.service;
import java.util.List;

import com.accenture.academico.Acc.Bank.exception.ConexaoBancoDadosException;
import com.accenture.academico.Acc.Bank.exception.EntidadeEmUsoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;

@Service
public class AgenciaService {

    private static final String MSG_AGENCIA_EM_USO = "Agência de código %d não pode ser removida, " + "pois possui clientes ativos";

    private static final String MSG_CONEXAO_BD_PERDIDA = "Falha na conexão com o banco de dados. Tente novamente mais tarde.";

    @Autowired
    private ModelMapper modelMapper;
	
    @Autowired
    private AgenciaRepository agenciaRepository;

    public Agencia buscarAgencia(Long agenciaId) {
        try {
            return agenciaRepository.findById(agenciaId)
                    .orElseThrow(() -> new AgenciaNaoEncontradaException(agenciaId));
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public Agencia atualizarAgencia(Long agenciaId, AgenciaRequestDTO agenciaDTO) {
        try {
            Agencia agencia = buscarAgencia(agenciaId);
            Agencia agenciaAtualizada = converterParaAgencia(agenciaDTO);
            agenciaAtualizada.setId(agencia.getId());
            return agenciaRepository.save(agenciaAtualizada);
        } catch (EmptyResultDataAccessException e) {
            throw new AgenciaNaoEncontradaException(agenciaId);
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public Agencia criarAgencia(AgenciaRequestDTO agenciaDTO) {
        Agencia agencia = null;
        try {
            agencia = converterParaAgencia(agenciaDTO);
            return agenciaRepository.save(agencia);
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public void removerAgencia(Long id) {
        try{
            agenciaRepository.delete(buscarAgencia(id));
        } catch (EmptyResultDataAccessException e) {
            throw new AgenciaNaoEncontradaException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_AGENCIA_EM_USO, id));
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }

    public List<Agencia> listarAgencias() {
        try {
            return agenciaRepository.findAll();
        } catch (DataAccessResourceFailureException e) {
            throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
        }
    }
    
    private Agencia converterParaAgencia(AgenciaRequestDTO agenciaDTO) {
    	return modelMapper.map(agenciaDTO, Agencia.class);
    }
}
