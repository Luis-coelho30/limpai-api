package br.com.limpai.projeto_limpai.dto;


import br.com.limpai.projeto_limpai.model.entity.Usuario;

public record RegistroDTO(Long id,
                          String nome,
                          Usuario usuario) {
}
