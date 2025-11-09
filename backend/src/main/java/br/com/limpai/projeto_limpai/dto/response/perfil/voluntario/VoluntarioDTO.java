package br.com.limpai.projeto_limpai.dto.response.perfil.voluntario;

import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;

import java.time.LocalDate;

public record VoluntarioDTO(String nome,
                            LocalDate dataNascimento,
                            String cpf,
                            String email,
                            String telefone) {

    public static VoluntarioDTO from(Voluntario voluntario, Usuario usuario) {
        return new VoluntarioDTO(voluntario.getNome(),
                voluntario.getDataNascimento(),
                voluntario.getCpf(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }
}
