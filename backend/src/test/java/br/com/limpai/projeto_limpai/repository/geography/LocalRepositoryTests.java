package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.model.geography.Local;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJdbcTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(statements = {
        "INSERT INTO \"estado\" (\"estado_id\", \"nome\", \"sigla\") VALUES (1, 'São Paulo', 'SP')",
        "INSERT INTO \"cidade\" (\"cidade_id\", \"nome\", \"estado_id\") VALUES (1, 'São Paulo', 1)"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class LocalRepositoryTests {

    @Autowired
    private LocalRepository localRepository;

    @Test
    @Commit
    @Order(1)
    void salvarLocal() {
        Local local = new Local();
        local.setNome("Praia Cristal");
        local.setCep("00000000");
        local.setEndereco("Rua dos Palmares 91");
        local.setCidadeId(1L);

        localRepository.save(local);

        assertTrue(localRepository.findById(local.getLocalId()).isPresent());
    }

    @Test
    @Order(2)
    void mostrarLocal() {
        Optional<Local> local = localRepository.findById(1L);
        Local localSalvo;

        assertTrue(local.isPresent());

        localSalvo = local.get();
        System.out.println(localSalvo.getLocalId());
        System.out.println(localSalvo.getNome());
        System.out.println(localSalvo.getEndereco());
        System.out.println(localSalvo.getCep());
        System.out.println(localSalvo.getCidadeId());
    }

    @Test
    @Order(3)
    void atualizarLocal() {
        Optional<Local> local = localRepository.findById(1L);
        Local localSalvo;

        if(local.isPresent()) {
            localSalvo = local.get();
            localSalvo.setNome("Praia Suja");
            localRepository.save(localSalvo);
        }

        if(localRepository.findById(1L).isPresent()) {
            assertEquals("Praia Suja", localRepository.findById(1L).get().getNome());
        } else {
            fail();
        }
    }

    @Test
    @Order(4)
    void deletarLocal() {
        Optional<Local> local = localRepository.findById(1L);
        Local localApagado;

        if(local.isPresent()) {
            localApagado = local.get();
            localRepository.delete(localApagado);
        }

        assertFalse(localRepository.findById(1L).isPresent());
    }
}
