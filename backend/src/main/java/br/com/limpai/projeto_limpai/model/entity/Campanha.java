package br.com.limpai.projeto_limpai.model.entity;

import br.com.limpai.projeto_limpai.model.geography.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Table("campanha")
public class Campanha {

    @Id
    @Column("campanha_id")
    private Long campanhaId;
    @Column("nome")
    private String nome;
    @Column("descricao")
    private String descricao;
    @Column("data_inicio")
    private LocalDateTime dataInicio;
    @Column("data_fim")
    private LocalDateTime dataFim;
    @Column("meta_fundos")
    private BigDecimal metaFundos = BigDecimal.ZERO;
    @Column("fundos_arrecadados")
    private BigDecimal fundosArrecadados = BigDecimal.ZERO;
    @Column("local_id")
    private Long localId;

    public Campanha() {
    }

    public Campanha(Long campanhaId, String nome, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim,
                    BigDecimal metaFundos, BigDecimal fundosArrecadados, Long localId) {
        this.campanhaId = campanhaId;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.metaFundos = metaFundos;
        this.fundosArrecadados = fundosArrecadados;
        this.localId = localId;
    }

    public Long getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Long campanhaId) {
        this.campanhaId = campanhaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getMetaFundos() {
        return metaFundos;
    }

    public void setMetaFundos(BigDecimal metaFundos) {
        this.metaFundos = metaFundos;
    }

    public BigDecimal getFundosArrecadados() {
        return fundosArrecadados;
    }

    public void setFundosArrecadados(BigDecimal fundosArrecadados) {
        this.fundosArrecadados = fundosArrecadados;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Campanha campanha = (Campanha) o;
        return Objects.equals(campanhaId, campanha.campanhaId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(campanhaId);
    }
}
