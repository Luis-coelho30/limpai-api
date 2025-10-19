package br.com.limpai.projeto_limpai.exception.user;

public class EmailJaCadastradoException extends RuntimeException{
    public EmailJaCadastradoException(String email) {
        super("O email '" + email + "' já está cadastrado.");
    }
}
