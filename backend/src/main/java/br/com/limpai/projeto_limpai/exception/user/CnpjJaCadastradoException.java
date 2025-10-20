package br.com.limpai.projeto_limpai.exception.user;

public class CnpjJaCadastradoException extends RuntimeException {
    public CnpjJaCadastradoException(String cnpj) {
        super("O email '" + cnpj + "' já está cadastrado.");
    }
}
