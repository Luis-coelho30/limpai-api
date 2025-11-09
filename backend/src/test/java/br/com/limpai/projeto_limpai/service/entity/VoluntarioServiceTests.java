package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.entity.AtualizarVoluntarioRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.voluntario.VoluntarioDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.voluntario.VoluntarioMinDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.VoluntarioCadastroDTO;
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

import java.time.LocalDate;
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
        v1.setDataNascimento(LocalDate.now());

        Voluntario v2 = new Voluntario();
        v2.setVoluntarioId(2L);
        v2.setNome("Brito");
        v2.setCpf("111.111.222-22");
        v2.setDataNascimento(LocalDate.now());

        List<Voluntario> voluntariosFake = List.of(v1, v2);

        Mockito.when(voluntarioRepository.findAll())
                .thenReturn(voluntariosFake);

        List<VoluntarioMinDTO> resultado = voluntarioService.listarVoluntarios();

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Claudio", resultado.getFirst().nome()),
                () -> assertEquals(LocalDate.now(), resultado.getFirst().dataNascimento()),

                () -> assertEquals("Brito", resultado.get(1).nome()),
                () -> assertEquals(LocalDate.now(), resultado.get(1).dataNascimento())
        );

    }

    @Test
    public void deveListarVoluntarioPublico() {
        Voluntario v1 = new Voluntario();
        v1.setVoluntarioId(1L);
        v1.setNome("Claudio");
        v1.setCpf("111.111.111-11");
        v1.setDataNascimento(LocalDate.now());

        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(v1));

        VoluntarioMinDTO voluntarioMinDTO = voluntarioService.getVoluntarioPublicoById(1L);

        assertAll(
                () -> assertEquals("Claudio", voluntarioMinDTO.nome()),
                () -> assertEquals(LocalDate.now(), voluntarioMinDTO.dataNascimento())
        );
    }

    @Test
    public void deveListarVoluntarioPrivado() {
        Voluntario v1 = new Voluntario();
        v1.setVoluntarioId(1L);
        v1.setNome("Claudio");
        v1.setCpf("111.111.111-11");
        v1.setDataNascimento(LocalDate.now());

        Usuario usuario;
        usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("11 11111-1111");
        usuario.setTipoUsuario(UsuarioEnum.VOLUNTARIO);

        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(v1));

        Mockito.when(usuarioService.getUsuarioPorId(1L))
                .thenReturn(usuario);

        VoluntarioDTO voluntarioDTO = voluntarioService.getVoluntarioPrivadoById(1L);

        assertAll(
                () -> assertEquals("Claudio", voluntarioDTO.nome()),
                () -> assertEquals(LocalDate.now(), voluntarioDTO.dataNascimento()),
                () -> assertEquals("111.111.111-11", voluntarioDTO.cpf()),
                () -> assertEquals("teste@email.com", voluntarioDTO.email()),
                () -> assertEquals("11 11111-1111", voluntarioDTO.telefone())
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

        VoluntarioCadastroDTO voluntarioDTO = new VoluntarioCadastroDTO(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getTelefone(),
                "Claudio",
                "111.111.111-11",
                LocalDate.now()
                );

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
                Mockito.any(LocalDate.class)
        );

        RegistroDTO registroDTO = voluntarioService.cadastrarVoluntario(voluntarioDTO);

        assertAll(
                () -> assertEquals(1L, registroDTO.id()),
                () -> assertEquals("Claudio", registroDTO.nome()),
                () -> assertEquals("teste@empresa.com", registroDTO.usuario().getEmail())
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
                LocalDate.now()
        );
    }

    @Test
    public void deveAtualizarVoluntario() {
        VoluntarioCadastroDTO voluntarioDTO = new VoluntarioCadastroDTO(
                "novoteste@empresa.com",
                "senha123",
                "11 55555-1111",
                "Joao",
                "111.111.111-11",
                LocalDate.now()
        );

        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(new Voluntario(1L,"Claudio", "111.111.111-11", LocalDate.now())));

        Mockito.when(usuarioService.
                        atualizarUsuario(1L, "novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.VOLUNTARIO))
                .thenReturn(new Usuario(1L,"novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.VOLUNTARIO));

        Mockito.when(voluntarioRepository.save(Mockito.any(Voluntario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegistroDTO registroDTO = voluntarioService.
                atualizarVoluntario(1L, voluntarioDTO);

        assertAll(
                () -> assertEquals(1L, registroDTO.id()),
                () -> assertEquals("Joao", registroDTO.nome()),
                () -> assertEquals("novoteste@empresa.com", registroDTO.usuario().getEmail())
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
    public void deveAtualizarVoluntarioParcialmente() {
        AtualizarVoluntarioRequestDTO voluntarioRequestDTO = new AtualizarVoluntarioRequestDTO(
                "Joao",
                "11 55555-1111",
                null,
                null
        );

        LocalDate data = LocalDate.now();

        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(new Voluntario(1L,"Claudio", "111.111.111-11", data)));

        Mockito.when(usuarioService.
                        atualizarTelefone(1L, "11 55555-1111"))
                .thenReturn(new Usuario(1L,"novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.VOLUNTARIO));

        Mockito.when(voluntarioRepository.save(Mockito.any(Voluntario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VoluntarioDTO voluntarioDTO = voluntarioService.
                atualizarParcial(1L, voluntarioRequestDTO);

        assertAll(
                () -> assertEquals("Joao", voluntarioDTO.nome()),
                () -> assertEquals("11 55555-1111", voluntarioDTO.telefone()),
                () -> assertEquals(data, voluntarioDTO.dataNascimento())
        );

        Mockito.verify(voluntarioRepository).findById(1L);

        Mockito.verify(usuarioService).atualizarTelefone(1L, "11 55555-1111");

        Mockito.verify(voluntarioRepository).save(Mockito.any(Voluntario.class));
    }

    @Test
    public void deveApagarVoluntario() {
        Voluntario existente = new Voluntario(1L, "Claudio", "111.111.111-11", LocalDate.now());
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
        Voluntario existente = new Voluntario(1L, "Claudio", "111.111.111-11", LocalDate.now());
        VoluntarioCadastroDTO voluntarioDTO = new VoluntarioCadastroDTO(
                "teste@email.com",
                "senha123",
                "11 11111-1111",
                "Claudio",
                "111.222.333-11",
                LocalDate.now()
        );

        VoluntarioCadastroDTO voluntarioAtualizadoDTO = new VoluntarioCadastroDTO(
                "novo@empresa.com",
                "senha1234",
                "11 55555-1111",
                "Joao",
                "111.222.333-11",
                LocalDate.now()
        );


        Mockito.when(voluntarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.when(voluntarioRepository.existsByCpf("111.222.333-11"))
                .thenReturn(true);

        Assertions.assertAll(
                () -> assertThrows(CpfJaCadastradoException.class, () ->
                        voluntarioService.cadastrarVoluntario(voluntarioDTO)
                ),

                () -> assertThrows(CpfJaCadastradoException.class, () ->
                        voluntarioService.atualizarVoluntario(1L, voluntarioAtualizadoDTO)
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
                () -> voluntarioService.getVoluntarioPublicoById(1L)
        );

        Mockito.verify(voluntarioRepository).findById(1L);
    }
}
