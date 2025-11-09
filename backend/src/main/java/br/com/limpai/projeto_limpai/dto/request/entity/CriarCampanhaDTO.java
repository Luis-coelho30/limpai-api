package br.com.limpai.projeto_limpai.dto.request.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarCampanhaDTO(String nome,
                               String descricao,
                               LocalDateTime dataInicio,
                               LocalDateTime dataFim,
                               BigDecimal metaFundos,
                               Long localId) {
}
