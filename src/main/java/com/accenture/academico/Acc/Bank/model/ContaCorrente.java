package com.accenture.academico.Acc.Bank.model;

import java.math.BigDecimal;
import java.util.List;

import com.accenture.academico.Acc.Bank.exception.contacorrente.SaldoInsuficienteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ValorInvalidoException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contas_correntes")
public class ContaCorrente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String numero;

	@Column(nullable = false)
	private BigDecimal saldo;

	@ManyToOne
	@JoinColumn(name = "id_agencia")
	private Agencia agencia;

	@OneToOne
	@JoinColumn(name = "id_cliente")
	@JsonBackReference
	private Cliente cliente;
	
	@OneToMany(mappedBy = "contaCorrente", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
    private List<Transacao> transacoes;

	public void sacar(BigDecimal valorSaque) {
		if (valorSaque.compareTo(BigDecimal.ZERO) <= 0) 
			throw new ValorInvalidoException();
		
		if (saldo.compareTo(valorSaque) < 0) 
			throw new SaldoInsuficienteException();
		
		this.saldo = this.saldo.subtract(valorSaque);
	}

	public void depositar(BigDecimal valorDeposito) {
		if (valorDeposito.compareTo(BigDecimal.ZERO) <= 0) 
			throw new ValorInvalidoException();
		
		this.saldo = this.saldo.add(valorDeposito);
	}
	
    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
        transacao.setContaCorrente(this);
    }
}
