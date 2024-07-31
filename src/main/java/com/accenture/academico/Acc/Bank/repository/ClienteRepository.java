package com.accenture.academico.Acc.Bank.repository;

import com.accenture.academico.Acc.Bank.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
