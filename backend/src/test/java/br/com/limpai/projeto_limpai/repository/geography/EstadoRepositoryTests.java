package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.model.geography.Estado;
import br.com.limpai.projeto_limpai.repository.AbstractIntegrationTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EstadoRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void deveRetornarEstado() {
        Optional<Estado> estado = estadoRepository.findById(1L);
        Estado estadoSalvo;

        assertTrue(estado.isPresent());

        estadoSalvo = estado.get();
        assertEquals(1L, estadoSalvo.estadoId());
        assertEquals("Acre", estadoSalvo.nome());
        assertEquals("AC", estadoSalvo.sigla());
    }
}
