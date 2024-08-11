package com.accenture.academico.Acc.Bank.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

	List<Transacao> findByContaCorrente(ContaCorrente contaCorrente);
	
	List<Transacao> findByContaCorrenteAndDataHoraBetween(ContaCorrente contaCorrente, LocalDateTime dataInicio, LocalDateTime dataFim);
	
}
