package br.com.limpai.projeto_limpai.exception.user;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String email) {
        super("Usuário com email: '" + email + "' não está cadastrado.");
    }
}
