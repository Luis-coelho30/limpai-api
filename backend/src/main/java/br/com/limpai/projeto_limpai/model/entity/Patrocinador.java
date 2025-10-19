package br.com.limpai.projeto_limpai.model.entity;

import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("patrocinador")
public class Patrocinador {

    @Id
    @Column("usuario_id")
    private Long patrocinadorId;
    @Column("razao_social")
    private String razaoSocial;
    @Column("nome_fantasia")
    private String nomeFantasia;
    @Column("cnpj")
    private String cnpj;

    public Patrocinador() {
    }

    public Patrocinador(Long patrocinadorId, String razaoSocial, String nomeFantasia, String cnpj) {
        this.patrocinadorId = patrocinadorId;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public Long getPatrocinadorId() {
        return patrocinadorId;
    }

    public void setPatrocinadorId(Long patrocinadorId) {
        this.patrocinadorId = patrocinadorId;
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
        return Objects.equals(patrocinadorId, patrocinador.patrocinadorId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(patrocinadorId);
    }
}
