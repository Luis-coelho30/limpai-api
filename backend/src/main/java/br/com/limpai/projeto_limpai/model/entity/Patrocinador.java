package br.com.limpai.projeto_limpai.model.entity;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Patrocinador {

    @Id
    private Long usuarioId;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;

    public Patrocinador() {
    }

    public Patrocinador(Long usuarioId, String razaoSocial, String nomeFantasia, String cnpj) {
        this.usuarioId = usuarioId;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patrocinador patrocinador = (Patrocinador) o;
        return Objects.equals(usuarioId, patrocinador.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usuarioId);
    }
}
