package br.com.limpai.projeto_limpai.dto;


import br.com.limpai.projeto_limpai.model.entity.Usuario;

public record PatrocinadorDTO(String id,
                              String nomeFantasia,
                              Usuario usuario) {
}
