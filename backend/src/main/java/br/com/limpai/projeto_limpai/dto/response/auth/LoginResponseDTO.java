package br.com.limpai.projeto_limpai.dto.response.auth;

public record LoginResponseDTO(String token,
                               UsuarioViewDTO usuario)
{ }
