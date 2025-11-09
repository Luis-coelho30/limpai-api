package br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador;

import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;

public record PatrocinadorDTO(String nomeFantasia,
                              String razaoSocial,
                              String cnpj,
                              String email,
                              String telefone
                              ) {

    public static PatrocinadorDTO from(Patrocinador patrocinador, Usuario usuario) {
        return new PatrocinadorDTO(patrocinador.getNomeFantasia(),
                patrocinador.getRazaoSocial(),
                patrocinador.getCnpj(),
                usuario.getEmail(),
                usuario.getTelefone());
    }
}
