package com.accenture.academico.Acc.Bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.Acc.Bank.model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    
    boolean existsByTelefone(String telefone);
    
    boolean existsByEmail(String email);
}
