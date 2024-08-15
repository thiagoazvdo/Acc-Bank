package com.accenture.academico.Acc.Bank.repository;

import com.accenture.academico.Acc.Bank.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
	boolean existsByTelefone(String telefone);
}
