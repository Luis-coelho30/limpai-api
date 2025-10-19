package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Campanha;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJdbcTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(statements = {
        "INSERT INTO \"estado\"(\"estado_id\", \"nome\", \"sigla\") VALUES (1, 'São Paulo', 'SP')",
        "INSERT INTO \"cidade\"(\"cidade_id\", \"nome\", \"estado_id\") VALUES (1, 'Campinas', 1)",
        "INSERT INTO \"local\"(\"local_id\", \"nome\", \"endereco\", \"cidade_id\", \"cep\") VALUES " +
        "(1, 'Parque Central', 'Rua X, 123', 1, '13000000')"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class CampanhaRepositoryTests {

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Test
    @Commit
    @Order(1)
    void salvarCampanha() {
        Campanha campanha = new Campanha();
        campanha.setNome("Praia Limpa");
        campanha.setDataFim(LocalDateTime.now().plusSeconds(30));
        campanha.setDataInicio(LocalDateTime.now());
        campanha.setDescricao("Esta é uma campanha");
        campanha.setMetaFundos(BigDecimal.TEN);
        campanha.setLocalId(1L);

        campanhaRepository.save(campanha);

        assertTrue(campanhaRepository.findById(campanha.getCampanhaId()).isPresent());
    }

    @Test
    @Order(2)
    void mostrarCampanha() {
        Optional<Campanha> campanha = campanhaRepository.findById(1L);
        Campanha campanhaSalva;

        assertTrue(campanha.isPresent());

        campanhaSalva = campanha.get();
        System.out.println(campanhaSalva.getCampanhaId());
        System.out.println(campanhaSalva.getNome());
        System.out.println(campanhaSalva.getDataInicio());
        System.out.println(campanhaSalva.getDataFim());
        System.out.println(campanhaSalva.getDescricao());
        System.out.println(campanhaSalva.getMetaFundos());
        System.out.println(campanhaSalva.getFundosArrecadados());
        System.out.println(campanhaSalva.getLocalId());
    }

    @Test
    @Order(3)
    void atualizarCampanha() {
        Optional<Campanha> campanha = campanhaRepository.findById(1L);
        Campanha campanhaSalva;

        if(campanha.isPresent()) {
            campanhaSalva = campanha.get();
            campanhaSalva.setNome("Praia Suja");
            campanhaRepository.save(campanhaSalva);
        }

        if(campanhaRepository.findById(1L).isPresent()) {
            assertEquals("Praia Suja", campanhaRepository.findById(1L).get().getNome());
        } else {
            fail();
        }
    }

    @Test
    @Order(4)
    void deletarCampanha() {
        Optional<Campanha> campanha = campanhaRepository.findById(1L);
        Campanha campanhaApagada;

        if(campanha.isPresent()) {
            campanhaApagada = campanha.get();
            campanhaRepository.delete(campanhaApagada);
        }

        assertFalse(campanhaRepository.findById(1L).isPresent());
    }
}
