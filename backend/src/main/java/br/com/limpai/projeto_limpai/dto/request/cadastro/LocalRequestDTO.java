package br.com.limpai.projeto_limpai.dto.request.cadastro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LocalRequestDTO(
        @NotBlank(message = "O nome do local não pode estar vazio")
        String nome,
        @NotBlank(message = "O endereço é obrigatório")
        String endereco,
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter apenas 8 dígitos numéricos")
        String cep,
        @NotNull(message = "O ID da cidade é obrigatório")
        Long cidadeId
)
{ }
