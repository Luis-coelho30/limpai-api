package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.EmailJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.UsuarioRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveListarUsuarioPorId() {
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setUsuarioId(1L);
        usuarioSalvo.setEmail("teste@limpai.com");
        usuarioSalvo.setSenha("123");
        usuarioSalvo.setTelefone("11 11111-1111");
        usuarioSalvo.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioSalvo));

        Usuario resultado = usuarioService.getUsuarioPorId(1L);

        assertAll(
                () -> assertEquals(1L, resultado.getUsuarioId()),
                () -> assertEquals("teste@limpai.com", resultado.getEmail()),
                () -> assertEquals("123", resultado.getSenha()),
                () -> assertEquals("11 11111-1111", resultado.getTelefone()),
                () -> assertEquals(UsuarioEnum.VOLUNTARIO, resultado.getTipoUsuario())
        );

        Mockito.verify(usuarioRepository).findById(1L);
    }

    @Test
    void cadastrarUsuarioGeral() {
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setUsuarioId(1L);
        usuarioSalvo.setEmail("teste@limpai.com");
        usuarioSalvo.setSenha("123");
        usuarioSalvo.setTelefone("11 11111-1111");

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarioSalvo);

        Usuario resultado = usuarioService.criarUsuarioBase(usuarioSalvo.getEmail(), usuarioSalvo.getSenha(),
                usuarioSalvo.getTelefone(),usuarioSalvo.getTipoUsuario());

        assertNotNull(resultado.getUsuarioId());
        assertEquals("teste@limpai.com", resultado.getEmail());
        Mockito.verify(usuarioRepository).save(Mockito.any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoSeEmailJaExistir() {
        Mockito.when(usuarioRepository.findByEmail("teste@limpai.com"))
                .thenReturn(Optional.of(new Usuario()));

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setUsuarioId(1L);
        usuarioExistente.setEmail("outro@limpai.com");
        usuarioExistente.setSenha("123");
        usuarioExistente.setTelefone("11 11111-1111");
        usuarioExistente.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        Mockito.when(usuarioRepository.existsByEmail("teste@limpai.com"))
                .thenReturn(true);

        Assertions.assertAll(
                () -> assertThrows(EmailJaCadastradoException.class, () ->
                        usuarioService.criarUsuarioBase("teste@limpai.com", "123", "11 11111-1111", UsuarioEnum.VOLUNTARIO)
                ),
                () -> assertThrows(EmailJaCadastradoException.class, () ->
                        usuarioService.atualizarUsuario(1L, "teste@limpai.com", "123", "11 11111-1111", UsuarioEnum.VOLUNTARIO)
                )
        );

        Mockito.verify(usuarioRepository).findByEmail("teste@limpai.com");
        Mockito.verify(usuarioRepository).findById(1L);
        Mockito.verify(usuarioRepository).existsByEmail("teste@limpai.com");
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoFoiEncontrado() {
        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertAll(
                () -> assertThrows(UsuarioNaoEncontradoException.class, () ->
                        usuarioService.atualizarUsuario(1L, "teste@limpai.com", "123", "11 11111-1111", UsuarioEnum.VOLUNTARIO)
                ),
                () -> assertThrows(UsuarioNaoEncontradoException.class, () ->
                        usuarioService.apagarUsuario(1L)
                )
        );
    }

    @Test
    void deveAtualizarUsuarioSeExistir() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setUsuarioId(1L);
        usuarioExistente.setEmail("teste@limpai.com");
        usuarioExistente.setSenha("123");
        usuarioExistente.setTelefone("11 11111-1111");
        usuarioExistente.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.atualizarUsuario(1L, "novo@limpai.com", "456", "11 11111-1111", UsuarioEnum.VOLUNTARIO);

        assertEquals(1L, resultado.getUsuarioId());
        assertEquals("novo@limpai.com", resultado.getEmail());
        assertEquals("456", resultado.getSenha());
        assertEquals("11 11111-1111", resultado.getTelefone());
        assertEquals(UsuarioEnum.VOLUNTARIO, resultado.getTipoUsuario());

        Mockito.verify(usuarioRepository).findById(1L);
        Mockito.verify(usuarioRepository).save(Mockito.any(Usuario.class));
    }

    @Test
    void deveExcluirUsuarioSeExistir() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setUsuarioId(1L);
        usuarioExistente.setEmail("teste@limpai.com");
        usuarioExistente.setSenha("123");
        usuarioExistente.setTelefone("11 11111-1111");
        usuarioExistente.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        usuarioService.apagarUsuario(1L);

        Mockito.verify(usuarioRepository).findById(1L);
        Mockito.verify(usuarioRepository).delete(usuarioExistente);
    }

    @Test
    void deveCarregarUsuarioPorEmailSeExistir() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setUsuarioId(1L);
        usuarioExistente.setEmail("teste@limpai.com");
        usuarioExistente.setSenha("123");
        usuarioExistente.setTelefone("11 11111-1111");
        usuarioExistente.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(usuarioRepository.findByEmail("teste@limpai.com"))
                .thenReturn(Optional.of(usuarioExistente));

        Usuario usuario = usuarioService.carregarUsuarioPorEmail("teste@limpai.com");

        assertAll(
                () -> assertEquals(1L, usuario.getUsuarioId()),
                () -> assertEquals("teste@limpai.com", usuario.getEmail()),
                () -> assertEquals("123", usuario.getSenha()),
                () -> assertEquals("11 11111-1111", usuario.getTelefone()),
                () -> assertEquals(UsuarioEnum.VOLUNTARIO, usuario.getTipoUsuario())
        );

        Mockito.verify(usuarioRepository).findByEmail("teste@limpai.com");
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoExistir() {
        Mockito.when(usuarioRepository.findByEmail("naoexiste@limpai.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> usuarioService.carregarUsuarioPorEmail("naoexiste@limpai.com")
        );

        Mockito.verify(usuarioRepository).findByEmail("naoexiste@limpai.com");
    }
}
