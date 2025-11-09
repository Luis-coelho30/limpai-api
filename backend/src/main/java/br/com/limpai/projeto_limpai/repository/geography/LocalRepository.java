package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;
import br.com.limpai.projeto_limpai.model.geography.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalRepository extends ListCrudRepository<Local, Long>, PagingAndSortingRepository<Local, Long> {

    boolean existsByCepAndEndereco(String cep, String endereco);

    @Query("""
        SELECT
            l.local_id, l.nome, l.endereco, l.cep, l.cidade_id,
            c.nome AS cidade_nome,
            e.sigla AS estado_sigla
        FROM local l
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE l.local_id = :id
    """)
    Optional<LocalResponseDTO> findLocalById(@Param("id") Long id);

    @Query("""
        SELECT
            l.local_id, l.nome, l.endereco, l.cep, l.cidade_id,
            c.nome AS cidade_nome,
            e.sigla AS estado_sigla
        FROM local l
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
    """)
    List<LocalResponseDTO> findAllLocal(Pageable pageable);

    @Query("""
        SELECT
            l.local_id, l.nome, l.endereco, l.cep, l.cidade_id,
            c.nome AS cidade_nome,
            e.sigla AS estado_sigla
        FROM local l
        INNER JOIN cidade c ON l.cidade_id = c.cidade_id
        INNER JOIN estado e ON c.estado_id = e.estado_id
        WHERE l.cidade_id = :cidadeId
    """)
    List<LocalResponseDTO> findLocalByCidade(@Param("cidadeId") Long cidadeId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM local WHERE cidade_id = :cidadeId")
    long countByCidadeId(@Param("cidadeId") Long cidadeId);
}
