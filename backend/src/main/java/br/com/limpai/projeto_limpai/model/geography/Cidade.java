package br.com.limpai.projeto_limpai.model.geography;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("cidade")
public record Cidade (
        @Id
        @Column("cidade_id")
        Long cidadeId,
        @Column("nome")
        String nome,
        @Column("estado_id")
        Long estadoId) { }