package br.com.limpai.projeto_limpai.exception.geography;

public class LocalJaCadastradoException extends RuntimeException {
    public LocalJaCadastradoException(String endereco, String cep) {
        super("O local com endereco: " + endereco + " e CEP: " + cep + " já foi cadastrado!");
    }
}
