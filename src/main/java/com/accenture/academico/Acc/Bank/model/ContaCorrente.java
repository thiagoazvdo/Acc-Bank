package com.accenture.academico.Acc.Bank.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.accenture.academico.Acc.Bank.exception.contacorrente.SaldoInsuficienteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ValorInvalidoException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "contas_correntes")
public class ContaCorrente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = true, unique = true, length = 5)
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
	
	@JsonIgnore
	@OneToMany(mappedBy = "contaCorrente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes;
	
	public ContaCorrente(Agencia agencia, Cliente cliente) {
		this.saldo = BigDecimal.ZERO;
		this.agencia = agencia;
		this.cliente = cliente;
		this.transacoes = new ArrayList<>();
	}

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
