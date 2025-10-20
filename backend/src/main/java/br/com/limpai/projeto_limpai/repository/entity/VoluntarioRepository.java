package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VoluntarioRepository extends CrudRepository<Voluntario, Long> {

    @Modifying
    @Query("INSERT INTO \"voluntario\"(\"usuario_id\", \"nome\", \"cpf\", \"data_nascimento\") " +
            "VALUES (:usuarioId, :nome, :cpf, :dataNascimento)")
    void insertVoluntario(@Param("usuarioId") Long usuarioId,
                            @Param("nome") String nome,
                            @Param("cpf") String cpf,
                            @Param("dataNascimento") LocalDateTime dataNascimento);

    boolean existsByCpf(String cpf);
}
