package com.accenture.academico.Acc.Bank.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteComSaldoException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteNaoEncontradaException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.SaldoInsuficienteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.TransferenciaEntreContasIguaisException;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.TipoTransacao;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.repository.ContaCorrenteRepository;
import com.accenture.academico.Acc.Bank.repository.TransacaoRepository;

@Service
public class ContaCorrenteService {

	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	public ContaCorrente buscarContaCorrente(Long id) {
		return contaCorrenteRepository.findById(id).orElseThrow(() -> new ContaCorrenteNaoEncontradaException(id));
	}
	
	public ContaCorrente buscarContaCorrentePorNumero(String numeroConta) {
		return contaCorrenteRepository.findByNumero(numeroConta).orElseThrow(() -> new ContaCorrenteNaoEncontradaException(numeroConta));
	}
	
	public void removerContaCorrente(Long id) {
		ContaCorrente conta = buscarContaCorrente(id);
		
		if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) 
			throw new ContaCorrenteComSaldoException();
		
		contaCorrenteRepository.delete(conta);
	}
	
	@Transactional
    public void sacar(Long id, SaqueDepositoRequestDTO saqueDTO) {
		ContaCorrente conta = buscarContaCorrente(id);
		verificaSeContaPossuiSaldoSuficiente(conta, saqueDTO.getValor());
		conta.sacar(saqueDTO.getValor());
		
		registrarTransacao(conta, null, TipoTransacao.SAQUE, saqueDTO.getValor(), saqueDTO.getDescricao());
		contaCorrenteRepository.save(conta);
    }

    @Transactional
    public void depositar(Long id, SaqueDepositoRequestDTO depositoDTO) {
    	ContaCorrente conta = buscarContaCorrente(id);
    	conta.depositar(depositoDTO.getValor());
    	
    	registrarTransacao(conta, null, TipoTransacao.DEPOSITO, depositoDTO.getValor(), depositoDTO.getDescricao());
    	contaCorrenteRepository.save(conta);
    }

    @Transactional
	public void transferir(Long idOrigem, TransferenciaRequestDTO transferenciaDTO) {
        ContaCorrente contaOrigem = buscarContaCorrente(idOrigem);
        ContaCorrente contaDestino = buscarContaCorrentePorNumero(transferenciaDTO.getNumeroContaDestino());
        
        if (contaOrigem.equals(contaDestino))
    		throw new TransferenciaEntreContasIguaisException();
        
        verificaSeContaPossuiSaldoSuficiente(contaOrigem, transferenciaDTO.getValor());
        contaOrigem.sacar(transferenciaDTO.getValor());
        contaDestino.depositar(transferenciaDTO.getValor());
        
        contaCorrenteRepository.save(contaOrigem);
        contaCorrenteRepository.save(contaDestino);
        
        registrarTransacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA_ENVIADA, transferenciaDTO.getValor(), transferenciaDTO.getDescricao());
        registrarTransacao(contaDestino, contaOrigem, TipoTransacao.TRANSFERENCIA_RECEBIDA, transferenciaDTO.getValor(), transferenciaDTO.getDescricao());
	}
    
    private void registrarTransacao(ContaCorrente contaOrigem, ContaCorrente contaDestino, TipoTransacao tipo, BigDecimal valor, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setContaCorrente(contaOrigem);
        transacao.setContaCorrenteRelacionada(contaDestino);
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setDescricao(descricao);
        
        transacaoRepository.save(transacao);
    }
	
	private void verificaSeContaPossuiSaldoSuficiente(ContaCorrente conta, BigDecimal valorSaque) {
		if (conta.getSaldo().compareTo(valorSaque) < 0) 
			throw new SaldoInsuficienteException();
	}
}
