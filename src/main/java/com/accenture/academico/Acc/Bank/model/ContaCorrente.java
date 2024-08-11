package com.accenture.academico.Acc.Bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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

    @Column(name = "data_criacao", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataCriacao;


	@OneToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	@JsonBackReference
	private Cliente cliente;
	
	@OneToMany(mappedBy = "contaCorrente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes;
	
	public ContaCorrente(Cliente cliente) {
		this.cliente = cliente;
		this.numero = Long.toString(cliente.getId() + 10000);
		this.saldo = BigDecimal.ZERO;
		this.transacoes = new ArrayList<>();
	}

	public void sacar(BigDecimal valorSaque) {
		boolean valorMaiorQueZero = valorSaque.compareTo(BigDecimal.ZERO) > 0;
		boolean saldoDisponivel = saldo.compareTo(valorSaque) >= 0;
		
		if (valorMaiorQueZero && saldoDisponivel) 
			this.saldo = this.saldo.subtract(valorSaque);
	}

	public void depositar(BigDecimal valorDeposito) {
		boolean valorMaiorQueZero = valorDeposito.compareTo(BigDecimal.ZERO) > 0;
		
		if (valorMaiorQueZero) 
			this.saldo = this.saldo.add(valorDeposito);
	}
	
    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
        transacao.setContaCorrente(this);
    }

    @PrePersist
    public void prePersist() {
        final LocalDateTime atual = LocalDateTime.now();
        this.dataCriacao = atual;
    }
}
