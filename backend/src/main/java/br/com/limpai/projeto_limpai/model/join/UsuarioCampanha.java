package br.com.limpai.projeto_limpai.model.join;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

public class UsuarioCampanha {

    private Long campanhaId;
    private Long usuarioId;
    private LocalDateTime dataInscricao;

    public UsuarioCampanha() {
    }

    public UsuarioCampanha(Long campanhaId, Long usuarioId, LocalDateTime dataInscricao) {
        this.campanhaId = campanhaId;
        this.usuarioId = usuarioId;
        this.dataInscricao = dataInscricao;
    }

    public Long getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Long campanhaId) {
        this.campanhaId = campanhaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
}
