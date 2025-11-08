package br.com.limpai.projeto_limpai.dto.response.auth;

public record UsuarioViewDTO(String id,
                             String nome,
                             String email,
                             String role) {
}
