package br.com.limpai.projeto_limpai.repository.join;

import br.com.limpai.projeto_limpai.model.join.UsuarioCampanha;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
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

    public List<UsuarioCampanha> findAllByUsuario(Long usuarioId) {
        String sql = """
        SELECT *
        FROM "usuario_campanha"
        WHERE "usuario_id" = ?
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UsuarioCampanha uc = new UsuarioCampanha();
            uc.setUsuarioId(rs.getLong("usuario_id"));
            uc.setCampanhaId(rs.getLong("campanha_id"));
            uc.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());
            return uc;
        }, usuarioId);
    }

    public List<UsuarioCampanha> findAllByCampanha(Long campanhaId) {
        String sql = """
        SELECT *
        FROM "usuario_campanha"
        WHERE "campanha_id" = ?
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UsuarioCampanha uc = new UsuarioCampanha();
            uc.setUsuarioId(rs.getLong("usuario_id"));
            uc.setCampanhaId(rs.getLong("campanha_id"));
            uc.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());
            return uc;
        }, campanhaId);
    }

    public void inscrever(UsuarioCampanha usuarioCampanha) {
        String sql = """
            INSERT INTO "usuario_campanha" ("usuario_id", "campanha_id", "data_inscricao")
            VALUES (?, ?, ?)
        """;
        jdbcTemplate.update(sql, usuarioCampanha.getUsuarioId(), usuarioCampanha.getCampanhaId(), Timestamp.valueOf(usuarioCampanha.getDataInscricao()));
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

    public void removerHistoricoDeInscricoesByCampanha(Long campanhaId) {
        String sql = """
            DELETE FROM "usuario_campanha"
            WHERE "campanha_id" = ?
        """;
        jdbcTemplate.update(sql, campanhaId);
    }
}
