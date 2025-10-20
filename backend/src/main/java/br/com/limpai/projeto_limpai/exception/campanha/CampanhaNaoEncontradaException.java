package br.com.limpai.projeto_limpai.exception.campanha;

public class CampanhaNaoEncontradaException extends RuntimeException {
    public CampanhaNaoEncontradaException(Long id) {
        super("Campanha com ID: " + id + " não foi encontrada!");
    }
}
