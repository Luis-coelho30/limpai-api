package br.com.limpai.projeto_limpai.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Objects;

@Table("voluntario")
public class Voluntario {

    @Id
    @Column("usuario_id")
    private Long voluntarioId;
    @Column("nome")
    private String nome;
    @Column("cpf")
    private String cpf;
    @Column("data_nascimento")
    private LocalDate dataNascimento;

    public Voluntario() {
    }

    public Voluntario(Long voluntarioId, String nome, String cpf, LocalDate dataNascimento) {
        this.voluntarioId = voluntarioId;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public Long getVoluntarioId() {
        return voluntarioId;
    }

    public void setVoluntarioId(Long voluntarioId) {
        this.voluntarioId = voluntarioId;
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
        return Objects.equals(voluntarioId, voluntario.voluntarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(voluntarioId);
    }
}
