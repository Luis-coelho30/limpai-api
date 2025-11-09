package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.dto.internal.CampanhaProjection;
import br.com.limpai.projeto_limpai.dto.response.perfil.campanha.CampanhaMinDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.inscricao.MinhaInscricaoDTO;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampanhaRepository extends ListCrudRepository<Campanha, Long>, PagingAndSortingRepository<Campanha, Long> {

    @Query("""
        SELECT CASE WHEN data_fim < CURRENT_TIMESTAMP THEN TRUE ELSE FALSE END
        FROM campanha
        WHERE campanha_id = :campanhaId
    """)
    boolean isExpired(@Param("campanhaId") Long campanhaId);

    @Query("SELECT COUNT(*) > 0 FROM campanha WHERE campanha_id = :campanhaId AND patrocinador_id = :patrocinadorId")
    boolean isPatrocinadorDono(@Param("campanhaId") Long campanhaId, @Param("patrocinadorId") Long patrocinadorId);

    @Modifying
    @Query("UPDATE campanha SET fundos_arrecadados = fundos_arrecadados + :valor WHERE campanha_id = :id")
    void adicionarFundos(@Param("id") Long id, @Param("valor") BigDecimal valor);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.local_id, l.nome as local_nome, l.endereco, l.cep,
            c.cidade_id, c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN local l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.campanha_id = :campanhaId
    """)
    Optional<CampanhaProjection> findCampanhaById(@Param("campanhaId") Long campanhaId);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.patrocinador_id = :patrocinadorId
    """)
    List<CampanhaMinDTO> findByPatrocinadorId(@Param("patrocinadorId") Long patrocinadorId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id,
            cam.nome AS nome_campanha,
            cam.data_fim,
            l.nome AS local_nome,
            c.nome AS cidade_nome,
            e.sigla AS estado_sigla,
            uc.data_inscricao
        FROM usuario_campanha uc
        INNER JOIN campanha cam ON uc.campanha_id = cam.campanha_id
        INNER JOIN local l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE uc.usuario_id = :usuarioId
    """)
    List<MinhaInscricaoDTO> findInscricoesByUsuario(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
    """)
    List<CampanhaMinDTO> findCampanhasNaoExpiradas(Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
    """)
    List<CampanhaMinDTO> findCampanhasExpiradas(Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
          AND c.cidade_id = :cidadeId
    """)
    List<CampanhaMinDTO> findAtivasByCidade(@Param("cidadeId") Long cidadeId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
          AND c.cidade_id = :cidadeId
    """)
    List<CampanhaMinDTO> findExpiradasByCidade(@Param("cidadeId") Long cidadeId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
          AND e.estado_id = :estadoId
    """)
    List<CampanhaMinDTO> findAtivasByEstado(@Param("estadoId") Long estadoId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
          AND e.estado_id = :estadoId
    """)
    List<CampanhaMinDTO> findExpiradasByEstado(@Param("estadoId") Long estadoId, Pageable pageable);

    @Query("""
        SELECT COUNT(*)
        FROM campanha cam
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
    """)
    long countCampanhasNaoExpiradas();

    @Query("""
        SELECT COUNT(*)
        FROM campanha cam
        WHERE cam.data_fim < CURRENT_TIMESTAMP
    """)
    long countCampanhasExpiradas();

    @Query("""
        SELECT COUNT(cam.campanha_id)
        FROM campanha cam
        INNER JOIN local l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
          AND c.cidade_id = :cidadeId
    """)
    long countAtivasByCidade(@Param("cidadeId") Long cidadeId);

    @Query("""
        SELECT COUNT(cam.campanha_id)
        FROM campanha cam
        INNER JOIN local l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
          AND c.cidade_id = :cidadeId
    """)
    long countExpiradasByCidade(@Param("cidadeId") Long cidadeId);

    @Query("""
        SELECT COUNT(cam.campanha_id)
        FROM campanha cam
        INNER JOIN local l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
          AND e.estado_id = :estadoId
    """)
    long countAtivasByEstado(@Param("estadoId") Long estadoId);

    @Query("""
        SELECT COUNT(cam.campanha_id)
        FROM campanha cam
        INNER JOIN local l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
          AND e.estado_id = :estadoId
    """)
    long countExpiradasByEstado(@Param("estadoId") Long estadoId);

    @Query("SELECT COUNT(*) FROM campanha WHERE patrocinador_id = :patrocinadorId")
    long countByPatrocinadorId(@Param("patrocinadorId") Long patrocinadorId);

    @Query("SELECT COUNT(*) FROM usuario_campanha WHERE usuario_id = :usuarioId")
    long countInscricoesByUsuario(@Param("usuarioId") Long usuarioId);
}
