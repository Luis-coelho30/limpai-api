package br.com.limpai.projeto_limpai.dto.response.perfil.voluntario;

import br.com.limpai.projeto_limpai.model.entity.Voluntario;

import java.time.LocalDate;

public record VoluntarioMinDTO(String nome,
                               LocalDate dataNascimento) {

    public static VoluntarioMinDTO from(Voluntario voluntario) {
        return new VoluntarioMinDTO(voluntario.getNome(), voluntario.getDataNascimento());
    }
}
