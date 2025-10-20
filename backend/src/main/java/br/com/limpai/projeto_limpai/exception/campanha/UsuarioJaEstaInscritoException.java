package br.com.limpai.projeto_limpai.exception.campanha;

public class UsuarioJaEstaInscritoException extends RuntimeException {
    public UsuarioJaEstaInscritoException(Long usuarioId, Long campanhaId) {
        super("O usuario com ID: " + usuarioId + " já está inscrito na campanha de ID: " + campanhaId + "!");
    }
}
