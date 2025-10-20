package br.com.limpai.projeto_limpai.exception.user;

public class CnpjJaCadastradoException extends RuntimeException {
    public CnpjJaCadastradoException(String cnpj) {
        super("O CNPJ: '" + cnpj + "' já está cadastrado.");
    }
}
