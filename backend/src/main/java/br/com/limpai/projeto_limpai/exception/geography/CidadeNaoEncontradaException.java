package br.com.limpai.projeto_limpai.exception.geography;

public class CidadeNaoEncontradaException extends RuntimeException {
    public CidadeNaoEncontradaException(Long id) {
        super("A cidade com ID " + id + " não existe.");
    }
}
