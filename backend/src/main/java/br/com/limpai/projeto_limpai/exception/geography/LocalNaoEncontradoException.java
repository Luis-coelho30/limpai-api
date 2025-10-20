package br.com.limpai.projeto_limpai.exception.geography;

public class LocalNaoEncontradoException extends RuntimeException {
    public LocalNaoEncontradoException(Long id) {
        super("O Local com ID: '" + id + "' não foi encontrado.");
    }
}
