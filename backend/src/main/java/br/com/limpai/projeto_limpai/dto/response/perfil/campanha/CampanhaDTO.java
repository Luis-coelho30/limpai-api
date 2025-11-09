package br.com.limpai.projeto_limpai.dto.response.perfil.campanha;

import br.com.limpai.projeto_limpai.dto.internal.CampanhaProjection;
import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaDTO(
        Long campanhaId,
        String nome,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        BigDecimal metaFundos,
        BigDecimal fundosArrecadados,
        Long qtdInscritos,
        LocalResponseDTO localDTO
) {
    public static CampanhaDTO from(CampanhaProjection campanhaProjection) {
        return new CampanhaDTO(
                campanhaProjection.campanhaId(),
                campanhaProjection.nome(),
                campanhaProjection.descricao(),
                campanhaProjection.dataInicio(),
                campanhaProjection.dataFim(),
                campanhaProjection.metaFundos(),
                campanhaProjection.fundosArrecadados(),
                campanhaProjection.qtdInscritos(),
                new LocalResponseDTO(
                        campanhaProjection.localId(),
                        campanhaProjection.localNome(),
                        campanhaProjection.endereco(),
                        campanhaProjection.cep(),
                        campanhaProjection.cidadeId(),
                        campanhaProjection.cidadeNome(),
                        campanhaProjection.estadoSigla()
                )
        );
    }
}
