package com.accenture.academico.Acc.Bank.service;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaJaCadastradaException;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    public Agencia buscarAgencia(Long agenciaId) {
        return agenciaRepository.findById(agenciaId).orElseThrow(() -> new AgenciaNaoEncontradaException(agenciaId));
    }

    public Agencia atualizarAgencia(Long agenciaId, AgenciaRequestDTO agenciaDTO){
        Agencia agencia = buscarAgencia(agenciaId);
        BeanUtils.copyProperties(agenciaDTO, agencia);
        return agenciaRepository.save(agencia);
    }

    public Agencia criarAgencia(AgenciaRequestDTO agenciaDTO) {
    	verificaSeTelefoneJaCadastrado(agenciaDTO.getTelefone());
    	
    	Agencia agencia = new Agencia();
    	BeanUtils.copyProperties(agenciaDTO, agencia);
        return agenciaRepository.save(agencia);
    }

    public void removerAgencia(Long id) {
        agenciaRepository.delete(buscarAgencia(id));
    }

    public List<Agencia> listarAgencias(){
        return agenciaRepository.findAll();
    }
    
    private void verificaSeTelefoneJaCadastrado(String telefone) {
    	if (agenciaRepository.existsByTelefone(telefone)) 
			throw new AgenciaJaCadastradaException("telefone", telefone);
    }
}
