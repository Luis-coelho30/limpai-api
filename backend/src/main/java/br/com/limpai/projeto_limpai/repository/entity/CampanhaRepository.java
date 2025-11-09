package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.dto.response.perfil.CampanhaViewDTO;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampanhaRepository extends ListCrudRepository<Campanha, Long>, PagingAndSortingRepository<Campanha, Long> {

    @Query("""
        SELECT CASE WHEN "data_fim" < CURRENT_TIMESTAMP THEN TRUE ELSE FALSE END
        FROM "campanha"
        WHERE "campanha_id" = :campanhaId
    """)
    boolean isExpired(@Param("campanhaId") Long campanhaId);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome, l.endereco, l.cep,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
    """)
    Page<CampanhaViewDTO> findCampanhasNaoExpiradas(Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome, l.endereco, l.cep,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
    """)
    Page<CampanhaViewDTO> findCampanhasExpiradas(Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome, l.endereco, l.cep,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
          AND c.cidade_id = :cidadeId
    """)
    Page<CampanhaViewDTO> findAtivasByCidade(@Param("cidadeId") Long cidadeId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome, l.endereco, l.cep,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
          AND c.cidade_id = :cidadeId
    """)
    Page<CampanhaViewDTO> findExpiradasByCidade(@Param("cidadeId") Long cidadeId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome, l.endereco, l.cep,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim >= CURRENT_TIMESTAMP
          AND e.estado_id = :estadoId
    """)
    Page<CampanhaViewDTO> findAtivasByEstado(@Param("estadoId") Long estadoId, Pageable pageable);

    @Query("""
        SELECT
            cam.campanha_id, cam.nome, cam.descricao, cam.data_inicio,
            cam.data_fim, cam.meta_fundos, cam.fundos_arrecadados,
            (SELECT COUNT(*) FROM usuario_campanha uc WHERE uc.campanha_id = cam.campanha_id) AS qtd_inscritos,
            l.nome as local_nome, l.endereco, l.cep,
            c.nome as cidade_nome,
            e.sigla as estado_sigla
        FROM campanha cam
        INNER JOIN "local" l ON cam.local_id = l.local_id
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE cam.data_fim < CURRENT_TIMESTAMP
          AND e.estado_id = :estadoId
    """)
    Page<CampanhaViewDTO> findExpiradasByEstado(@Param("estadoId") Long estadoId, Pageable pageable);
}
