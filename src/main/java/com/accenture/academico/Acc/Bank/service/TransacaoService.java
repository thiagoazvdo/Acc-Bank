package com.accenture.academico.Acc.Bank.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.repository.TransacaoRepository;

@Service
public class TransacaoService {

	@Autowired
	private ContaCorrenteService contaCorrenteService;
	
	@Autowired
	private TransacaoRepository transacaoRepository;

	public List<Transacao> obterExtratoGeral(Long contaId){
    	ContaCorrente conta = contaCorrenteService.buscarContaCorrente(contaId);
    	return transacaoRepository.findByContaCorrente(conta);
    }
	
	public List<Transacao> obterExtratoFiltrado(Long contaId, LocalDateTime dataInicio, LocalDateTime dataFim){
		ContaCorrente conta = contaCorrenteService.buscarContaCorrente(contaId);
		return transacaoRepository.findByContaCorrenteAndDataHoraBetween(conta, dataInicio, dataFim);
	}
    
    public List<Transacao> obterExtratoMensal(Long contaId, YearMonth mesAno){
    	LocalDateTime mesInicio = mesAno.atDay(1).atStartOfDay();
    	LocalDateTime mesFim = mesAno.atEndOfMonth().atTime(23, 59, 59);
    	
    	return obterExtratoFiltrado(contaId, mesInicio, mesFim);
    }
    
    public List<Transacao> obterExtratoAnual(Long contaId, int ano){
    	LocalDateTime anoInicio = LocalDateTime.of(ano, 1, 1, 0, 0);
    	LocalDateTime anoFim = LocalDateTime.of(ano, 12, 31, 23, 59);
    	
    	return obterExtratoFiltrado(contaId, anoInicio, anoFim);
    }
    
}
