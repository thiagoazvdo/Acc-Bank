package com.accenture.academico.Acc.Bank.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "agencias")
@Schema(description = "Entidade que representa uma agência bancária")
public class Agencia {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da agência", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome da agência", example = "Agência Central")
    private String nome;

    @Column(nullable = false)
    @Schema(description = "Endereço da agência", example = "Rua Principal, 123")
    private String endereco;

    @Column(nullable = false, unique = true, length = 11)
    @Schema(description = "Telefone da agência", example = "11987654321")
    private String telefone;
    
    @Column(name = "data_criacao", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    
    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        final LocalDateTime atual = LocalDateTime.now();
        this.dataCriacao = atual;
        this.dataAtualizacao = atual;
    }
}
