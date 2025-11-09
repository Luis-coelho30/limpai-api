package br.com.limpai.projeto_limpai.dto.request.entity;

import java.time.LocalDate;

public record AtualizarVoluntarioRequestDTO(String nome,
                                            String telefone,
                                            String cpf,
                                            LocalDate dataNascimento) {
}
