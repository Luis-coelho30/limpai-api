package br.com.limpai.projeto_limpai.exception.campanha;

public class UsuarioNaoEstaInscritoException extends RuntimeException {
    public UsuarioNaoEstaInscritoException(Long usuarioId, Long campanhaId) {
        super("O Usuário com ID: " + usuarioId + " não está inscrito na campanha de ID: " + campanhaId + "!");
    }
}
