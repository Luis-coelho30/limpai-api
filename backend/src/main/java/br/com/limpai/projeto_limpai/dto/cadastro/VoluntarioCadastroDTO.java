package br.com.limpai.projeto_limpai.dto.cadastro;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record VoluntarioCadastroDTO (String email,
                                     String senha,
                                     String telefone,
                                     String nome,
                                     String cpf,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                     LocalDate dataNascimento)
{ }
