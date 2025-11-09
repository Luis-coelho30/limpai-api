package br.com.limpai.projeto_limpai.dto.response.local;

public record LocalResponseDTO(
        Long id,
        String nome,
        String endereco,
        String cep,
        Long cidadeId,
        String cidadeNome,
        String estadoSigla
) { }
