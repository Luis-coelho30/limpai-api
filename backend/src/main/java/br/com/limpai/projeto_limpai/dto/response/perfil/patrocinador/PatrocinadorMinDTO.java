package br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador;

import br.com.limpai.projeto_limpai.model.entity.Patrocinador;

public record PatrocinadorMinDTO(String nomeFantasia,
                                 String razaoSocial) {

    public static PatrocinadorMinDTO from(Patrocinador patrocinador) {
        return new PatrocinadorMinDTO(patrocinador.getNomeFantasia(), patrocinador.getRazaoSocial());
    }
}
