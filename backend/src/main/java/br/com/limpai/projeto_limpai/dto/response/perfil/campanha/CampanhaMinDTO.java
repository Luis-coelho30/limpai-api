package br.com.limpai.projeto_limpai.dto.response.perfil.campanha;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaMinDTO(
        String nome,
        LocalDateTime dataFim,
        BigDecimal metaFundos,
        BigDecimal fundosArrecadados,
        Long qtdInscritos,
        String localNome,
        String cidadeNome,
        String estadoSigla
)
{ }
