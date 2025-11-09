package br.com.limpai.projeto_limpai.dto.internal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaProjection(
        Long campanhaId,
        String nome,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        BigDecimal metaFundos,
        BigDecimal fundosArrecadados,
        Long qtdInscritos,
        Long localId,
        String localNome,
        String endereco,
        String cep,
        Long cidadeId,
        String cidadeNome,
        String estadoSigla
)
{ }