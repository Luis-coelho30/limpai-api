package br.com.limpai.projeto_limpai.exception;

import br.com.limpai.projeto_limpai.exception.campanha.UsuarioJaEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioNaoEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.geography.LocalJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<String> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaEstaInscritoException.class)
    public ResponseEntity<String> handleUsuarioJaCadastrado(UsuarioJaEstaInscritoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());
    }

    @ExceptionHandler(LocalJaCadastradoException.class)
    public ResponseEntity<String> handleLocalJaCadastrado(LocalJaCadastradoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());
    }

    @ExceptionHandler(CnpjJaCadastradoException.class)
    public ResponseEntity<String> handleCnpjJaCadastrado(CnpjJaCadastradoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<String> handleCpfJaCadastrado(CpfJaCadastradoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<String> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(ex.getMessage());
    }

    @ExceptionHandler(LocalNaoEncontradoException.class)
    public ResponseEntity<String> handleLocalNaoEncontrado(LocalNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEstaInscritoException.class)
    public ResponseEntity<String> handleUsuarioNaoInscrito(UsuarioNaoEstaInscritoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ex.getMessage());
    }

    @ExceptionHandler(CredenciaisIncorretasException.class)
    public ResponseEntity<String> handleCredenciaisIncorretas(CredenciaisIncorretasException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // 401
                .body(ex.getMessage());
    }

    @ExceptionHandler(JwtParsingException.class)
    public ResponseEntity<String> handleJwtParsing(JwtParsingException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ex.getMessage());
    }
}
