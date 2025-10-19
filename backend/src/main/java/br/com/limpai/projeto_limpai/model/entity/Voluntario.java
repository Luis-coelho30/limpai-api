package br.com.limpai.projeto_limpai.model.entity;

import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
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
    private LocalDateTime dataNascimento;

    public Voluntario() {
    }

    public Voluntario(Long voluntarioId, String nome, String cpf, LocalDateTime dataNascimento) {
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

    public LocalDateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateTime dataNascimento) {
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
