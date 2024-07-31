package com.accenture.academico.Acc.Bank.service;
import com.accenture.academico.Acc.Bank.exception.agencia.AgenciaNaoEncontradoException;
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
        return agenciaRepository.findById(agenciaId).orElseThrow(() -> new AgenciaNaoEncontradoException());
    }

    public Agencia atualizarAgencia(Long agenciaId, Agencia agencia){
        Agencia agenciaAtual = buscarAgencia(agenciaId);
        agenciaAtual.setNome(agencia.getNome());
        agenciaAtual.setEndereco(agencia.getEndereco());
        agenciaAtual.setTelefone(agencia.getTelefone());
        return agenciaRepository.save(agenciaAtual);
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
