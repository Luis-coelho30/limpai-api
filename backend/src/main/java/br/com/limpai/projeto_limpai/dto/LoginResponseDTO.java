package br.com.limpai.projeto_limpai.dto;

public record LoginResponseDTO(String token,
                               UsuarioDTO usuario
) { }
