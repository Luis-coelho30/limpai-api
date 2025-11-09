package br.com.limpai.projeto_limpai.dto.response.perfil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaViewDTO(
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
        String estadoSigla)
{ }
