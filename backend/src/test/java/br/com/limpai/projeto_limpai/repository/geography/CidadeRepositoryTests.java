package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.model.geography.Cidade;
import br.com.limpai.projeto_limpai.repository.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CidadeRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Test
    void deveRetornarCidade() {
        Optional<Cidade> cidade = cidadeRepository.findById(1L);
        Cidade cidadeSalva;

        assertTrue(cidade.isPresent());

        cidadeSalva = cidade.get();
        assertEquals(1L, cidadeSalva.cidadeId());
        assertEquals("Rio Branco", cidadeSalva.nome());
        assertEquals(1L, cidadeSalva.estadoId());
    }
}
