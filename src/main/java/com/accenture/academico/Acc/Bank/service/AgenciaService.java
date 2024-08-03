package com.accenture.academico.Acc.Bank.service;
import com.accenture.academico.Acc.Bank.dto.AgenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradaException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    public Agencia buscarAgencia(Long agenciaId) {
        return agenciaRepository.findById(agenciaId).orElseThrow(() -> new AgenciaNaoEncontradaException(agenciaId));
    }

    public Agencia atualizarAgencia(Long agenciaId, AgenciaRequestDTO agenciaDTO){
        Agencia agenciaAtual = buscarAgencia(agenciaId);
        if (agenciaDTO.getNome() == null) agenciaDTO.setNome(agenciaAtual.getNome());
        if (agenciaDTO.getEndereco() == null) agenciaDTO.setEndereco(agenciaAtual.getEndereco());
        if (agenciaDTO.getTelefone() == null) agenciaDTO.setTelefone(agenciaAtual.getTelefone());
        return agenciaRepository.save(agenciaDTO.toEntity());
    }

    public Agencia criarAgencia(Agencia agencia) {
        return agenciaRepository.save(agencia);
    }

    public void removerAgencia(Long id) {
        agenciaRepository.delete(buscarAgencia(id));
    }

    public List<Agencia> listarAgencias(){
        return agenciaRepository.findAll();
    }
}
