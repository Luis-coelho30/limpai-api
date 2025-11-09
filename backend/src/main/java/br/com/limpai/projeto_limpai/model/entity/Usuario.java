package br.com.limpai.projeto_limpai.model.entity;

import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Usuario {

    @Id
    private Long usuarioId;
    private String email;
    private String senha;
    private String telefone;
    private UsuarioEnum tipo;

    public Usuario() {
    }

    public Usuario(Long usuarioId, String email, String senha, String telefone, UsuarioEnum tipo) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.tipo = tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public UsuarioEnum getTipo() {
        return tipo;
    }

    public void setTipo(UsuarioEnum tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(usuarioId, usuario.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usuarioId);
    }
}
