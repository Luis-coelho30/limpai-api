package br.com.limpai.projeto_limpai.dto.response.perfil;

import br.com.limpai.projeto_limpai.model.entity.Patrocinador;

public record PatrocinadorDTO(String nomeFantasia,
                              String razaoSocial) {

    public static PatrocinadorDTO from(Patrocinador patrocinador) {
        return new PatrocinadorDTO(patrocinador.getNomeFantasia(), patrocinador.getRazaoSocial());
    }
}
