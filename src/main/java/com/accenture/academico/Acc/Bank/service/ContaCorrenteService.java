package com.accenture.academico.Acc.Bank.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.academico.Acc.Bank.dto.ContaCorrenteRequestDTO;
import com.accenture.academico.Acc.Bank.exception.cliente.ClienteNaoEncontradoException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaComSaldoException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteNaoExisteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.TransferenciaEntreContasIguaisException;
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
	
	@Transactional
	public ContaCorrente criarContaCorrente(ContaCorrenteRequestDTO contaDTO) {
		Agencia agencia = agenciaRepository.findById(contaDTO.getIdAgencia())
				.orElseThrow(() -> new IllegalArgumentException("Agencia não existe."));
		
		Cliente cliente = clienteRepository.findById(contaDTO.getIdCliente())
				.orElseThrow(() -> new ClienteNaoEncontradoException());
		
		ContaCorrente conta = ContaCorrente.builder()
				.numero(gerarNumeroContaCorrente())
				.saldo(BigDecimal.ZERO)
				.agencia(agencia)
				.cliente(cliente)
				.build();
		
		return contaCorrenteRepository.save(conta);
	}

	private String gerarNumeroContaCorrente() {
		return Long.toString(contaCorrenteRepository.count() + 1);
	}

	public ContaCorrente buscarContaCorrente(Long id) {
		return contaCorrenteRepository.findById(id)
				.orElseThrow(() -> new ContaCorrenteNaoExisteException(id));
	}
	
	public void deletarContaCorrente(Long id) {
		ContaCorrente conta = buscarContaCorrente(id);
		
		if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) 
			throw new ContaComSaldoException();
		
		contaCorrenteRepository.delete(conta);
	}
	
	@Transactional
    public void sacar(Long id, BigDecimal valor) {
		ContaCorrente conta = buscarContaCorrente(id);
		
		conta.sacar(valor);
		contaCorrenteRepository.save(conta);
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
    	ContaCorrente conta = buscarContaCorrente(id);
    	
    	conta.depositar(valor);
    	contaCorrenteRepository.save(conta);
    }

    @Transactional
	public void transferir(Long idOrigem, Long idDestino, BigDecimal valor) {
    	if (idOrigem == idDestino)
    		throw new TransferenciaEntreContasIguaisException();
    	
        ContaCorrente contaOrigem = buscarContaCorrente(idOrigem);
        ContaCorrente contaDestino = buscarContaCorrente(idDestino);
        
        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);
        
        contaCorrenteRepository.save(contaOrigem);
        contaCorrenteRepository.save(contaDestino);
	}
}
