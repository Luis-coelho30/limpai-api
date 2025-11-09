package br.com.limpai.projeto_limpai.dto.response.perfil.inscricao;

import java.time.LocalDateTime;

public record MinhaInscricaoDTO(
        Long campanhaId,
        String nomeCampanha,
        LocalDateTime dataFim,
        String localNome,
        String cidadeNome,
        String estadoSigla,
        LocalDateTime dataInscricao
) {}