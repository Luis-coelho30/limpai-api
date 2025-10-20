package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.model.geography.Cidade;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(statements = {
        "INSERT INTO \"estado\" (\"estado_id\", \"nome\", \"sigla\") VALUES (1, 'São Paulo', 'SP')",
        "INSERT INTO \"cidade\" (\"cidade_id\", \"nome\", \"estado_id\") VALUES (1, 'São Paulo', 1)"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class CidadeRepositoryTests {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Test
    @Order(1)
    void mostrarEstado() {
        Optional<Cidade> cidade = cidadeRepository.findById(1L);
        Cidade cidadeSalva;

        assertTrue(cidade.isPresent());

        cidadeSalva = cidade.get();
        System.out.println(cidadeSalva.cidadeId());
        System.out.println(cidadeSalva.nome());
        System.out.println(cidadeSalva.estadoId());
    }

    @Test
    @Order(2)
    void deletarCidade() {
        Optional<Cidade> cidade = cidadeRepository.findById(1L);
        Cidade cidadeApagada;

        if(cidade.isPresent()) {
            cidadeApagada = cidade.get();
            cidadeRepository.delete(cidadeApagada);
        }

        assertFalse(cidadeRepository.findById(1L).isPresent());
    }
}
