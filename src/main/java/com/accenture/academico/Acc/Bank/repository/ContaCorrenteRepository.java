package com.accenture.academico.Acc.Bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.Acc.Bank.model.ContaCorrente;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long>{

	Optional<ContaCorrente> findByNumero(String numero);

	Optional<ContaCorrente> findByClienteId(Long clienteId);
}
