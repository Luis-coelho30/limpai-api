package br.com.limpai.projeto_limpai.dto.response.perfil.campanha;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaDTO(
        String nome,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        BigDecimal metaFundos,
        BigDecimal fundosArrecadados,
        Long qtdInscritos,
        String localNome,
        String endereco,
        String cep,
        String cidadeNome,
        String estadoSigla
)
{ }
