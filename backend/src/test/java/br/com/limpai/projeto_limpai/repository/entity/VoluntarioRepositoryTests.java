package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJdbcTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VoluntarioRepositoryTests {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @Commit
    @Order(1)
    void salvarVoluntario() {
        Usuario u = new Usuario();
        u.setEmail("voluntario@teste.com");
        u.setSenha("123");
        u.setTelefone("11 99999-9999");
        u.setTipoUsuario(UsuarioEnum.VOLUNTARIO);
        usuarioRepository.save(u);

        voluntarioRepository.insertVoluntario(u.getUsuarioId(), "Luis", "111.111.111-11", LocalDateTime.now());

        assertTrue(voluntarioRepository.findById(u.getUsuarioId()).isPresent());
    }

    @Test
    @Order(2)
    void mostrarVoluntario() {
        Optional<Voluntario> voluntario = voluntarioRepository.findById(1L);
        Voluntario voluntarioSalvo;

        assertTrue(voluntario.isPresent());

        voluntarioSalvo = voluntario.get();
        System.out.println(voluntarioSalvo.getVoluntarioId());
        System.out.println(voluntarioSalvo.getNome());
        System.out.println(voluntarioSalvo.getCpf());
        System.out.println(voluntarioSalvo.getDataNascimento());
    }

    @Test
    @Order(3)
    void atualizarVoluntario() {
        Optional<Voluntario> voluntario = voluntarioRepository.findById(1L);
        Voluntario voluntarioSalvo;

        if(voluntario.isPresent()) {
            voluntarioSalvo = voluntario.get();
            voluntarioSalvo.setNome("Pedro");
            voluntarioRepository.save(voluntarioSalvo);
        }

        if(voluntarioRepository.findById(1L).isPresent()) {
            assertEquals("Pedro", voluntarioRepository.findById(1L).get().getNome());
        } else {
            fail();
        }
    }

    @Test
    @Order(4)
    void deletarVoluntario() {
        Optional<Voluntario> voluntario = voluntarioRepository.findById(1L);
        Voluntario voluntarioApagado;

        if(voluntario.isPresent()) {
            voluntarioApagado = voluntario.get();
            voluntarioRepository.delete(voluntarioApagado);
        }

        assertFalse(voluntarioRepository.findById(1L).isPresent());
    }
}
