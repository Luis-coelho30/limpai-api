package br.com.limpai.projeto_limpai.exception.user;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String cpf) {
        super("O CPF: '" + cpf + "' já está cadastrado.");
    }
}
