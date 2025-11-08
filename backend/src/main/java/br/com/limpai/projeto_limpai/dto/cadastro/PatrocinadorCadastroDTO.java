package br.com.limpai.projeto_limpai.dto.cadastro;

public record PatrocinadorCadastroDTO (String email,
                                       String senha,
                                       String telefone,
                                       String razaoSocial,
                                       String nomeFantasia,
                                       String cnpj)
{ }
