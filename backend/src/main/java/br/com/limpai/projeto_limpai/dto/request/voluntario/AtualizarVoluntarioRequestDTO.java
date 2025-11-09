package br.com.limpai.projeto_limpai.dto.request.voluntario;

import java.time.LocalDate;

public record AtualizarVoluntarioRequestDTO(String nome,
                                            String telefone,
                                            String cpf,
                                            LocalDate dataNascimento) {
}
