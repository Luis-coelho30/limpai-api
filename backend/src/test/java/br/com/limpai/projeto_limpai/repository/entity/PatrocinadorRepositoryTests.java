package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatrocinadorRepositoryTests {

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @Commit
    @Order(1)
    void salvarPatrocinador() {
        Usuario u = new Usuario();
        u.setEmail("empresa@teste.com");
        u.setSenha("123");
        u.setTelefone("11 99999-9999");
        u.setTipoUsuario(UsuarioEnum.PATROCINADOR);
        usuarioRepository.save(u);

        patrocinadorRepository.insertPatrocinador(u.getUsuarioId(), "Empresa Teste Ltda", "Empresa Teste", "12345678000199");

        assertTrue(patrocinadorRepository.findById(u.getUsuarioId()).isPresent());
    }

    @Test
    @Order(2)
    void mostrarPatrocinador() {
        Optional<Patrocinador> patrocinador = patrocinadorRepository.findById(1L);
        Patrocinador patrocinadorSalvo;

        assertTrue(patrocinador.isPresent());

        patrocinadorSalvo = patrocinador.get();
        System.out.println(patrocinadorSalvo.getPatrocinadorId());
        System.out.println(patrocinadorSalvo.getCnpj());
        System.out.println(patrocinadorSalvo.getNomeFantasia());
        System.out.println(patrocinadorSalvo.getRazaoSocial());
    }

    @Test
    @Order(3)
    void atualizarPatrocinador() {
        Optional<Patrocinador> patrocinador = patrocinadorRepository.findById(1L);
        Patrocinador patrocinadorSalvo;

        if(patrocinador.isPresent()) {
            patrocinadorSalvo = patrocinador.get();
            patrocinadorSalvo.setNomeFantasia("McDonalds");
            patrocinadorRepository.save(patrocinadorSalvo);
        }

        if(patrocinadorRepository.findById(1L).isPresent()) {
            assertEquals("McDonalds", patrocinadorRepository.findById(1L).get().getNomeFantasia());
        } else {
            fail();
        }
    }

    @Test
    @Order(4)
    void deletarPatrocinador() {
        Optional<Patrocinador> patrocinador = patrocinadorRepository.findById(1L);
        Patrocinador patrocinadorApagado;

        if(patrocinador.isPresent()) {
            patrocinadorApagado = patrocinador.get();
            patrocinadorRepository.delete(patrocinadorApagado);
        }

        assertFalse(patrocinadorRepository.findById(1L).isPresent());
    }
}
