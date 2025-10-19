package br.com.limpai.projeto_limpai.repository.join;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Objects;

@Repository
public class UsuarioCampanhaRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioCampanhaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByUsuarioAndCampanha(Long usuarioId, Long campanhaId) {
        String sql = """
        SELECT COUNT(*)
        FROM "usuario_campanha"
        WHERE "usuario_id" = ? AND "campanha_id" = ?
    """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuarioId, campanhaId);
        return count != null && count > 0;
    }

    public void inscrever(Long usuarioId, Long campanhaId, Timestamp dataInscricao) {
        String sql = """
            INSERT INTO "usuario_campanha" ("usuario_id", "campanha_id", "data_inscricao")
            VALUES (?, ?, ?)
        """;
        jdbcTemplate.update(sql, usuarioId, campanhaId, dataInscricao);
    }

    public int contarInscricoesByUsuarioId(Long usuario_id) {
        String sql = """
            SELECT COUNT("usuario_id") FROM "usuario_campanha"
            WHERE "usuario_id" = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuario_id);

        return Objects.requireNonNullElse(count, 0);
    }

    public int contarInscricoesByCampanhaId(Long campanhaId) {
        String sql = """
            SELECT COUNT("campanha_id") FROM "usuario_campanha"
            WHERE "campanha_id" = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, campanhaId);

        return Objects.requireNonNullElse(count, 0);
    }

    public void removerInscricao(Long usuarioId, Long campanhaId) {
        String sql = """
            DELETE FROM "usuario_campanha"
            WHERE "usuario_id" = ? AND "campanha_id" = ?
        """;
        jdbcTemplate.update(sql, usuarioId, campanhaId);
    }
}
