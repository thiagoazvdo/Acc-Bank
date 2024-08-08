package com.accenture.academico.Acc.Bank.service;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;

@Service
public class AgenciaService {

    @Autowired
    private ModelMapper modelMapper;
	
    @Autowired
    private AgenciaRepository agenciaRepository;

    public Agencia buscarAgencia(Long agenciaId) {
        return agenciaRepository.findById(agenciaId).orElseThrow(() -> new AgenciaNaoEncontradaException(agenciaId));
    }

    public Agencia atualizarAgencia(Long agenciaId, AgenciaRequestDTO agenciaDTO){
        Agencia agencia = buscarAgencia(agenciaId);
        
        Agencia agenciaAtualizada = converterParaAgencia(agenciaDTO);
        agenciaAtualizada.setId(agencia.getId());
        
        return agenciaRepository.save(agenciaAtualizada);
    }

    public Agencia criarAgencia(AgenciaRequestDTO agenciaDTO) {
    	Agencia agencia = converterParaAgencia(agenciaDTO);
        return agenciaRepository.save(agencia);
    }

    public void removerAgencia(Long id) {
        agenciaRepository.delete(buscarAgencia(id));
    }

    public List<Agencia> listarAgencias(){
        return agenciaRepository.findAll();
    }
    
    private Agencia converterParaAgencia(AgenciaRequestDTO agenciaDTO) {
    	return modelMapper.map(agenciaDTO, Agencia.class);
    }
}
