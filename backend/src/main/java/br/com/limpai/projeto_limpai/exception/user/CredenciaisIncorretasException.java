package br.com.limpai.projeto_limpai.exception.user;

public class CredenciaisIncorretasException extends RuntimeException {
    public CredenciaisIncorretasException(String message) {
        super(message);
    }
}
