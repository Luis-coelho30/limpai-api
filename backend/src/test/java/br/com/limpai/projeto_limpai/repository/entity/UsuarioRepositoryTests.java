package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioRepositoryTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final String EMAIL = "teste@limpai.com";
    private final String SENHA = "123";
    private final String TELEFONE = "11 92151-1511";

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveSalvarNovoUsuario() {
        Usuario usuario = criarUsuarioPadrao();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        assertEquals(EMAIL, usuarioSalvo.getEmail());
        assertEquals(SENHA, usuarioSalvo.getSenha());
        assertEquals(TELEFONE, usuarioSalvo.getTelefone());
        assertEquals(UsuarioEnum.VOLUNTARIO, usuarioSalvo.getTipo());
    }

    @Test
    void deveEncontrarUsuarioPorId() {
        Usuario usuario = criarUsuarioPadrao();
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioSalvo.getUsuarioId());

        assertTrue(usuarioOpt.isPresent(), "O usuário deveria ter sido encontrado no banco");

        Usuario usuarioEncontrado = usuarioOpt.get();

        assertEquals(EMAIL, usuarioEncontrado.getEmail());
        assertEquals(SENHA, usuarioEncontrado.getSenha());
        assertEquals(TELEFONE, usuarioEncontrado.getTelefone());
        assertEquals(UsuarioEnum.VOLUNTARIO, usuarioEncontrado.getTipo());
    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        Usuario usuario = criarUsuarioPadrao();
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(usuarioSalvo.getEmail());

        assertTrue(usuarioOpt.isPresent(), "O usuário deveria ter sido encontrado no banco");

        Usuario usuarioEncontrado = usuarioOpt.get();

        assertEquals(EMAIL, usuarioEncontrado.getEmail());
        assertEquals(SENHA, usuarioEncontrado.getSenha());
        assertEquals(TELEFONE, usuarioEncontrado.getTelefone());
        assertEquals(UsuarioEnum.VOLUNTARIO, usuarioEncontrado.getTipo());
    }

    @Test
    void deveRetornarTrueSeEncontrarUsuarioPorEmail() {
        Usuario usuario = criarUsuarioPadrao();
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        assertTrue(usuarioRepository.existsByEmail(usuarioSalvo.getEmail()));
    }

    @Test
    void deveDeletarUsuario() {
        Usuario usuario = criarUsuarioPadrao();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        usuarioRepository.delete(usuarioSalvo);
    }

    @Test
    void deveDeletarUsuarioPorId() {
        Usuario usuario = criarUsuarioPadrao();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        usuarioRepository.deleteById(usuarioSalvo.getUsuarioId());
    }

    private Usuario criarUsuarioPadrao() {
        Usuario usuario = new Usuario();
        usuario.setEmail(EMAIL);
        usuario.setSenha(SENHA);
        usuario.setTelefone(TELEFONE);
        usuario.setTipo(UsuarioEnum.VOLUNTARIO);
        return usuario;
    }
}
