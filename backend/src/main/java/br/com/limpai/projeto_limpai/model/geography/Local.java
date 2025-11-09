package br.com.limpai.projeto_limpai.model.geography;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

public class Local {

    @Id
    private Long localId;
    private String nome;
    private String endereco;
    private String cep;
    private Long cidadeId;

    public Local() {
    }

    public Local(Long localId, String nome, String endereco, String cep, Long cidadeId) {
        this.localId = localId;
        this.nome = nome;
        this.endereco = endereco;
        this.cep = cep;
        this.cidadeId = cidadeId;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Long getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(Long cidadeId) {
        this.cidadeId = cidadeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Local local = (Local) o;
        return Objects.equals(localId, local.localId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(localId);
    }
}
