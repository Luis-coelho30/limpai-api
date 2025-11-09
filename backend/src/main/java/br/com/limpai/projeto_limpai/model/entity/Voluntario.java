package br.com.limpai.projeto_limpai.model.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

public class Voluntario {

    @Id
    private Long usuarioId;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;

    public Voluntario() {
    }

    public Voluntario(Long usuarioId, String nome, String cpf, LocalDate dataNascimento) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Voluntario voluntario = (Voluntario) o;
        return Objects.equals(usuarioId, voluntario.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usuarioId);
    }
}
