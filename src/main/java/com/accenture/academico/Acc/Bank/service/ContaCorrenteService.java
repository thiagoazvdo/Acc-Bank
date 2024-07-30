package com.accenture.academico.Acc.Bank.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.academico.Acc.Bank.dto.ContaCorrentePostPutRequestDTO;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteNaoExisteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.SaldoInsuficienteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ValorInvalidoException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.repository.AgenciaRepository;
import com.accenture.academico.Acc.Bank.repository.ClienteRepository;
import com.accenture.academico.Acc.Bank.repository.ContaCorrenteRepository;

@Service
public class ContaCorrenteService {

	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;
	
	@Autowired
	private AgenciaRepository agenciaRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public ContaCorrente criarContaCorrente(ContaCorrentePostPutRequestDTO contaDTO) {
		Agencia agencia = agenciaRepository.findById(contaDTO.getIdAgencia())
				.orElseThrow(() -> new IllegalArgumentException("Agencia não existe."));
		
		Cliente cliente = clienteRepository.findById(contaDTO.getIdCliente())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não existe."));
		
		ContaCorrente conta = ContaCorrente.builder()
				.numero(gerarNumeroContaCorrente())
				.agencia(agencia)
				.cliente(cliente)
				.build();
		
		return contaCorrenteRepository.save(conta);
	}

	private String gerarNumeroContaCorrente() {
		return Long.toString(contaCorrenteRepository.count() + 1);
	}

	public void deletarContaCorrente(Long id) {
		contaCorrenteRepository.deleteById(id);
	}
	
	public ContaCorrente buscarContaCorrente(Long id) {
		return contaCorrenteRepository.findById(id)
				.orElseThrow(() -> new ContaCorrenteNaoExisteException(id));
	}
	
	@Transactional
    public void sacar(Long id, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValorInvalidoException();
		
		ContaCorrente conta = buscarContaCorrente(id);
        
        if (conta.getSaldo().compareTo(valor) < 0)
            throw new SaldoInsuficienteException();
        
        conta.setSaldo(conta.getSaldo().subtract(valor));
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) 
            throw new ValorInvalidoException();
    	
    	ContaCorrente conta = buscarContaCorrente(id);
    	
        conta.setSaldo(conta.getSaldo().add(valor));
    }

    @Transactional
	public void transferir(Long idOrigem, Long idDestino, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) 
            throw new ValorInvalidoException();
        
        ContaCorrente contaOrigem = buscarContaCorrente(idOrigem);
        ContaCorrente contaDestino = buscarContaCorrente(idDestino);
        
        if (contaOrigem.getSaldo().compareTo(valor) < 0)
            throw new SaldoInsuficienteException();
        
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));        
	}
}
