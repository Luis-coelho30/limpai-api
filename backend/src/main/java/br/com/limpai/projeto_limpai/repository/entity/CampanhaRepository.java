package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Campanha;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampanhaRepository extends CrudRepository<Campanha, Long> {

    @Query("""
        SELECT CASE WHEN "data_fim" < CURRENT_TIMESTAMP THEN TRUE ELSE FALSE END
        FROM "campanha"
        WHERE "campanha_id" = :campanhaId
    """)
    boolean isExpired(@Param("campanhaId") Long campanhaId);
}
