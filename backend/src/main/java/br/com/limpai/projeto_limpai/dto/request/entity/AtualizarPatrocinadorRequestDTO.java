package br.com.limpai.projeto_limpai.dto.request.entity;

public record AtualizarPatrocinadorRequestDTO(String nomeFantasia,
                                              String razaoSocial,
                                              String cnpj,
                                              String telefone) {
}
