package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.model.geography.Estado;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJdbcTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(statements = {
        "INSERT INTO \"estado\" (\"estado_id\", \"nome\", \"sigla\") VALUES (1, 'São Paulo', 'SP')"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class EstadoRepositoryTests {

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    @Order(1)
    void mostrarEstado() {
        Optional<Estado> estado = estadoRepository.findById(1L);
        Estado estadoSalvo;

        assertTrue(estado.isPresent());

        estadoSalvo = estado.get();
        System.out.println(estadoSalvo.estadoId());
        System.out.println(estadoSalvo.nome());
        System.out.println(estadoSalvo.sigla());
    }

    @Test
    @Order(2)
    void deletarEstado() {
        Optional<Estado> estado = estadoRepository.findById(1L);
        Estado estadoApagado;

        if(estado.isPresent()) {
            estadoApagado = estado.get();
            estadoRepository.delete(estadoApagado);
        }

        assertFalse(estadoRepository.findById(1L).isPresent());
    }

}
