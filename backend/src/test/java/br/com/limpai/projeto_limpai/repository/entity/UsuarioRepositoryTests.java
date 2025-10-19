package br.com.limpai.projeto_limpai.repository.entity;

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
public class UsuarioRepositoryTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @Commit
    @Order(1)
    void salvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@limpai.com");
        usuario.setSenha("123");
        usuario.setTelefone("11 92151-1511");
        usuario.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        usuarioRepository.save(usuario);

        assertTrue(usuarioRepository.findById(usuario.getUsuarioId()).isPresent());
    }

    @Test
    @Order(2)
    void mostrarUsuario() {
        Optional<Usuario> usuario = usuarioRepository.findById(1L);
        Usuario usuarioSalvo;

        assertTrue(usuario.isPresent());

        usuarioSalvo = usuario.get();
        System.out.println(usuarioSalvo.getUsuarioId());
        System.out.println(usuarioSalvo.getEmail());
        System.out.println(usuarioSalvo.getSenha());
        System.out.println(usuarioSalvo.getTelefone());
        System.out.println(usuarioSalvo.getTipoUsuario());

    }

    @Test
    @Order(3)
    void atualizarUsuario() {
        Optional<Usuario> usuario = usuarioRepository.findById(1L);
        Usuario usuarioSalvo;

        if(usuario.isPresent()) {
            usuarioSalvo = usuario.get();
            usuarioSalvo.setEmail("abc@limpai.com");
            usuarioRepository.save(usuarioSalvo);
        }

        if(usuarioRepository.findById(1L).isPresent()) {
            assertEquals("abc@limpai.com", usuarioRepository.findById(1L).get().getEmail());
        } else {
            fail();
        }
    }

    @Test
    @Order(4)
    void deletarUsuario() {
        Optional<Usuario> usuario = usuarioRepository.findById(1L);
        Usuario usuarioApagado;

        if(usuario.isPresent()) {
            usuarioApagado = usuario.get();
            usuarioRepository.delete(usuarioApagado);
        }

        assertFalse(usuarioRepository.findById(1L).isPresent());
    }
}
