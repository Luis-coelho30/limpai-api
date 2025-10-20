package br.com.limpai.projeto_limpai.exception.campanha;

public class CampanhaExpiradaException extends RuntimeException {
    public CampanhaExpiradaException(Long campanhaId) {
        super("A campanha com ID: " + campanhaId + " já está expirada!");
    }
}
