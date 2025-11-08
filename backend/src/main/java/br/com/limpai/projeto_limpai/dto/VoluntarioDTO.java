package br.com.limpai.projeto_limpai.dto;

import br.com.limpai.projeto_limpai.model.entity.Voluntario;

import java.time.LocalDate;

public record VoluntarioDTO (String nome,
                             LocalDate dataNascimento) {

    public static VoluntarioDTO from(Voluntario voluntario) {
        return new VoluntarioDTO(voluntario.getNome(), voluntario.getDataNascimento());
    }
}
