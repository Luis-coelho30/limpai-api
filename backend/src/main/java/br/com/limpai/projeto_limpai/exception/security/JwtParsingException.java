package br.com.limpai.projeto_limpai.exception.security;

public class JwtParsingException extends RuntimeException {
    public JwtParsingException(String message) {
        super(message);
    }
}
