package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.CpfJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.repository.entity.VoluntarioRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class VoluntarioServiceTests {

    @Mock
    private VoluntarioRepository voluntarioRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private VoluntarioService voluntarioService;

    @Test
    public void deveListarVoluntarios() {
        Voluntario v1 = new Voluntario();
        v1.setVoluntarioId(1L);
        v1.setNome("Claudio");
        v1.setCpf("111.111.111-11");
        v1.setDataNascimento(LocalDateTime.MAX);

        Voluntario v2 = new Voluntario();
        v2.setVoluntarioId(2L);
        v2.setNome("Brito");
        v2.setCpf("111.111.222-22");
        v2.setDataNascimento(LocalDateTime.MAX);

        List<Voluntario> voluntariosFake = List.of(v1, v2);

        Mockito.when(voluntarioRepository.findAll())
                .thenReturn(voluntariosFake);

        List<Voluntario> resultado = voluntarioService.listarVoluntarios();

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(1L, resultado.getFirst().getVoluntarioId()),
                () -> assertEquals("Claudio", resultado.getFirst().getNome()),
                () -> assertEquals("111.111.111-11", resultado.getFirst().getCpf()),
                () -> assertEquals(LocalDateTime.MAX, resultado.getFirst().getDataNascimento()),

                () -> assertEquals(2L, resultado.get(1).getVoluntarioId()),
                () -> assertEquals("Brito", resultado.get(1).getNome()),
                () -> assertEquals("111.111.222-22", resultado.get(1).getCpf()),
                () -> assertEquals(LocalDateTime.MAX, resultado.get(1).getDataNascimento())
        );

    }

    @Test
    public void deveListarVoluntario() {
        Voluntario v1 = new Voluntario();
        v1.setVoluntarioId(1L);
        v1.setNome("Claudio");
        v1.setCpf("111.111.111-11");
        v1.setDataNascimento(LocalDateTime.MAX);

        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(v1));

        Voluntario voluntario = voluntarioService.getVoluntarioById(1L);

        assertAll(
                () -> assertEquals(1L, voluntario.getVoluntarioId()),
                () -> assertEquals("Claudio", voluntario.getNome()),
                () -> assertEquals("111.111.111-11", voluntario.getCpf()),
                () -> assertEquals(LocalDateTime.MAX, voluntario.getDataNascimento())
        );
    }

    @Test
    public void deveCadastrarVoluntario() {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setEmail("teste@empresa.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("11 11111-1111");
        usuario.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(usuarioService.criarUsuarioBase(
                        "teste@empresa.com",
                        "senha123",
                        "11 11111-1111",
                        UsuarioEnum.VOLUNTARIO))
                .thenReturn(usuario);

        Mockito.doNothing().when(voluntarioRepository).insertVoluntario(
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(LocalDateTime.class)
        );

        Voluntario voluntario = voluntarioService.cadastrarVoluntario("Claudio", "111.111.111-11", LocalDateTime.MAX,
                "teste@empresa.com", "senha123", "11 11111-1111");

        assertAll(
                () -> assertEquals(1L, voluntario.getVoluntarioId()),
                () -> assertEquals("Claudio", voluntario.getNome()),
                () -> assertEquals("111.111.111-11", voluntario.getCpf()),
                () -> assertEquals(LocalDateTime.MAX, voluntario.getDataNascimento())
        );

        Mockito.verify(usuarioService).criarUsuarioBase(
                "teste@empresa.com",
                "senha123",
                "11 11111-1111",
                UsuarioEnum.VOLUNTARIO);

        Mockito.verify(voluntarioRepository).insertVoluntario(
                usuario.getUsuarioId(),
                "Claudio",
                "111.111.111-11",
                LocalDateTime.MAX
        );
    }

    @Test
    public void deveAtualizarVoluntario() {
        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(new Voluntario(1L,"Claudio", "111.111.111-11", LocalDateTime.MAX)));

        Mockito.when(usuarioService.
                        atualizarUsuario(1L, "novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.VOLUNTARIO))
                .thenReturn(new Usuario(1L,"novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.VOLUNTARIO));

        Mockito.when(voluntarioRepository.save(Mockito.any(Voluntario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Voluntario voluntario = voluntarioService.
                atualizarVoluntario(1L, "Claudio", "111.111.111-11", LocalDateTime.MAX,
                        "novoteste@empresa.com", "senha123", "11 55555-1111");

        assertAll(
                () -> assertEquals(1L, voluntario.getVoluntarioId()),
                () -> assertEquals("Claudio", voluntario.getNome()),
                () -> assertEquals("111.111.111-11", voluntario.getCpf()),
                () -> assertEquals(LocalDateTime.MAX, voluntario.getDataNascimento())
        );

        Mockito.verify(voluntarioRepository).findById(1L);

        Mockito.verify(usuarioService).atualizarUsuario(
                1L,
                "novoteste@empresa.com",
                "senha123",
                "11 55555-1111",
                UsuarioEnum.VOLUNTARIO);

        Mockito.verify(voluntarioRepository).save(Mockito.any(Voluntario.class));
    }

    @Test
    public void deveApagarVoluntario() {
        Voluntario existente = new Voluntario(1L, "Claudio", "111.111.111-11", LocalDateTime.MAX);
        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.doNothing().when(usuarioService).apagarUsuario(existente.getVoluntarioId());

        voluntarioService.apagarVoluntario(1L);

        Mockito.verify(voluntarioRepository).findById(1L);
        Mockito.verify(voluntarioRepository).delete(existente);
        Mockito.verify(usuarioService).apagarUsuario(existente.getVoluntarioId());

    }

    @Test
    public void deveLancarExcecaoSeCpfExistir() {
        Voluntario existente = new Voluntario(1L, "Claudio", "111.111.111-11", LocalDateTime.MAX);
        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.when(voluntarioRepository.existsByCpf("111.222.333-11"))
                .thenReturn(true);

        Assertions.assertAll(
                () -> assertThrows(CpfJaCadastradoException.class, () ->
                        voluntarioService.cadastrarVoluntario(
                                "Claudio",
                                "111.222.333-11",
                                LocalDateTime.MAX,
                                "teste@empresa.com",
                                "senha123",
                                "11 11111-1111")
                ),

                () -> assertThrows(CpfJaCadastradoException.class, () ->
                        voluntarioService.atualizarVoluntario(
                                1L,
                                "Joao",
                                "111.222.333-11",
                                LocalDateTime.MIN,
                                "novo@empresa.com",
                                "senha1234",
                                "11 55555-1111"
                        )
                )
        );

        Mockito.verify(voluntarioRepository).findById(1L);
        Mockito.verify(voluntarioRepository, Mockito.times(2)).existsByCpf("111.222.333-11");
        Mockito.verifyNoMoreInteractions(usuarioService, voluntarioRepository);
    }

    @Test
    public void deveLancarExcecaoSeVoluntarioNaoExistir() {
        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> voluntarioService.getVoluntarioById(1L)
        );

        Mockito.verify(voluntarioRepository).findById(1L);
    }
}
