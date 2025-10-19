package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrocinadorRepository extends CrudRepository<Patrocinador, Long> {

    @Modifying
    @Query("INSERT INTO \"patrocinador\"(\"usuario_id\", \"razao_social\", \"nome_fantasia\", \"cnpj\") " +
            "VALUES (:usuarioId, :razaoSocial, :nomeFantasia, :cnpj)")
    void insertPatrocinador(@Param("usuarioId") Long usuarioId,
                            @Param("razaoSocial") String razaoSocial,
                            @Param("nomeFantasia") String nomeFantasia,
                            @Param("cnpj") String cnpj);
}
