package br.com.limpai.projeto_limpai.dto;

public record PatrocinadorCadastroDTO (String email,
                                       String senha,
                                       String telefone,
                                       String razaoSocial,
                                       String nomeFantasia,
                                       String cnpj)
{ }
