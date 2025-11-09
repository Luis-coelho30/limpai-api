package br.com.limpai.projeto_limpai.model.geography;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

public record Estado (
        @Id
        Long estadoId,
        String nome,
        String sigla) { }
