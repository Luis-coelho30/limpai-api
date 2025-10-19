package br.com.limpai.projeto_limpai.repository.join;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJdbcTest
@Import(UsuarioCampanhaRepository.class)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(statements = {
        "INSERT INTO \"usuario\" (\"email\", \"senha\", \"telefone\", \"tipo\") VALUES ('teste@limpai.com', '123', '11 11111-1111', 'VOLUNTARIO')",
        "INSERT INTO \"voluntario\" (\"usuario_id\", \"nome\", \"cpf\", \"data_nascimento\") VALUES (1, 'Luis', '123.111.111-14', '2025-01-01 10:30:00')",
        "INSERT INTO \"estado\" (\"estado_id\", \"nome\", \"sigla\") VALUES (1, 'São Paulo', 'SP')",
        "INSERT INTO \"cidade\" (\"cidade_id\", \"nome\", \"estado_id\") VALUES (1, 'São Paulo', 1)",
        "INSERT INTO \"local\" (\"nome\", \"endereco\", \"cep\", \"cidade_id\") VALUES ('Praia Cristal', 'Rua X', '00000000', 1)",
        "INSERT INTO \"campanha\" (\"nome\", \"descricao\", \"data_inicio\", \"data_fim\", \"local_id\") VALUES " +
                "('Praia Limpa', 'Limpar praia', '2025-01-10 10:30:00', '2025-02-10 18:00:00', '1')",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class UsuarioCampanhaRepositoryTests {

    @Autowired
    private UsuarioCampanhaRepository usuarioCampanhaRepository;

    @Test
    @Commit
    @Order(1)
    void salvarUsuarioCampanha() {
        usuarioCampanhaRepository.inscrever(1L, 1L, Timestamp.valueOf(LocalDateTime.now()));

        assertTrue(usuarioCampanhaRepository.existsByUsuarioAndCampanha(1L, 1L));
    }

    @Test
    @Order(2)
    void contarInscricoesPorUsuario() {
        assertEquals(1, usuarioCampanhaRepository.contarInscricoesByUsuarioId(1L));
    }

    @Test
    @Order(3)
    void contarInscricoesPorCampanha() {
        assertEquals(1, usuarioCampanhaRepository.contarInscricoesByCampanhaId(1L));
    }

    @Test
    @Order(4)
    void deletarUsuarioCampanha() {

        usuarioCampanhaRepository.removerInscricao(1L, 1L);

        assertFalse(usuarioCampanhaRepository.existsByUsuarioAndCampanha(1L, 1L));
    }
}
