package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.request.PatrocinadorRequestDTO;
import br.com.limpai.projeto_limpai.exception.user.CnpjJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.PatrocinadorRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PatrocinadorServiceTests {

    @Mock
    private PatrocinadorRepository patrocinadorRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PatrocinadorService patrocinadorService;

    @Test
    public void deveListarPatrocinadores() {
        Patrocinador p1 = new Patrocinador();
        p1.setPatrocinadorId(1L);
        p1.setRazaoSocial("Empresa A");
        p1.setNomeFantasia("A Fantasia");
        p1.setCnpj("12345678000199");

        Patrocinador p2 = new Patrocinador();
        p2.setPatrocinadorId(2L);
        p2.setRazaoSocial("Empresa B");
        p2.setNomeFantasia("B Fantasia");
        p2.setCnpj("98765432000188");

        List<Patrocinador> patrocinadoresFake = List.of(p1, p2);

        Mockito.when(patrocinadorRepository.findAll())
                .thenReturn(patrocinadoresFake);

        List<Patrocinador> resultado = patrocinadorService.listarPatrocinadores();

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(1L, resultado.getFirst().getPatrocinadorId()),
                () -> assertEquals("Empresa A", resultado.getFirst().getRazaoSocial()),
                () -> assertEquals("A Fantasia", resultado.getFirst().getNomeFantasia()),
                () -> assertEquals("12345678000199", resultado.getFirst().getCnpj()),

                () -> assertEquals(2L, resultado.get(1).getPatrocinadorId()),
                () -> assertEquals("Empresa B", resultado.get(1).getRazaoSocial()),
                () -> assertEquals("B Fantasia", resultado.get(1).getNomeFantasia()),
                () -> assertEquals("98765432000188", resultado.get(1).getCnpj())
        );

    }

    @Test
    public void deveListarPatrocinador() {
        Patrocinador p1 = new Patrocinador();
        p1.setPatrocinadorId(1L);
        p1.setRazaoSocial("Empresa A");
        p1.setNomeFantasia("A Fantasia");
        p1.setCnpj("12345678000199");

        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(p1));

        Patrocinador patrocinador = patrocinadorService.getPatrocinadorById(1L);

        assertAll(
                () -> assertEquals(1L, patrocinador.getPatrocinadorId()),
                () -> assertEquals("Empresa A", patrocinador.getRazaoSocial()),
                () -> assertEquals("A Fantasia", patrocinador.getNomeFantasia()),
                () -> assertEquals("12345678000199", patrocinador.getCnpj())
        );

    }

    @Test
    public void deveCadastrarPatrocinador() {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setEmail("teste@empresa.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("11 11111-1111");
        usuario.setTipoUsuario(UsuarioEnum.PATROCINADOR);

        Mockito.when(usuarioService.criarUsuarioBase(
                        "teste@empresa.com",
                        "senha123",
                        "11 11111-1111",
                        UsuarioEnum.PATROCINADOR))
                .thenReturn(usuario);

        Mockito.doNothing().when(patrocinadorRepository).insertPatrocinador(
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
        );

        Patrocinador patrocinador = patrocinadorService.cadastrarPatrocinador("Empresa A", "A Fantasia", "12345678000199",
                                                                            "teste@empresa.com", "senha123", "11 11111-1111");

        assertAll(
                () -> assertEquals(1L, patrocinador.getPatrocinadorId()),
                () -> assertEquals("Empresa A", patrocinador.getRazaoSocial()),
                () -> assertEquals("A Fantasia", patrocinador.getNomeFantasia()),
                () -> assertEquals("12345678000199", patrocinador.getCnpj())
        );

        Mockito.verify(usuarioService).criarUsuarioBase(
                "teste@empresa.com",
                "senha123",
                "11 11111-1111",
                UsuarioEnum.PATROCINADOR);

        Mockito.verify(patrocinadorRepository).insertPatrocinador(
                usuario.getUsuarioId(),
                "Empresa A",
                "A Fantasia",
                "12345678000199"
        );
    }

    @Test
    public void deveAtualizarPatrocinador() {
        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(new Patrocinador(1L,"Empresa A", "A Fantasia", "12345678000199")));

        Mockito.when(usuarioService.
                        atualizarUsuario(1L, "novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.PATROCINADOR))
                .thenReturn(new Usuario(1L,"novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.PATROCINADOR));

        Mockito.when(patrocinadorRepository.save(Mockito.any(Patrocinador.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Patrocinador patrocinador = patrocinadorService.
                atualizarPatrocinador(1L, "Empresa C", "C Fantasia", "12345678777111",
                                        "novoteste@empresa.com", "senha123", "11 55555-1111");

        assertAll(
                () -> assertEquals(1L, patrocinador.getPatrocinadorId()),
                () -> assertEquals("Empresa C", patrocinador.getRazaoSocial()),
                () -> assertEquals("C Fantasia", patrocinador.getNomeFantasia()),
                () -> assertEquals("12345678777111", patrocinador.getCnpj())
        );

        Mockito.verify(patrocinadorRepository).findById(1L);

        Mockito.verify(usuarioService).atualizarUsuario(
                1L,
                "novoteste@empresa.com",
                "senha123",
                "11 55555-1111",
                UsuarioEnum.PATROCINADOR);

        Mockito.verify(patrocinadorRepository).save(Mockito.any(Patrocinador.class));
    }

    @Test
    public void deveApagarPatrocinador() {
        Patrocinador existente = new Patrocinador(1L, "Empresa A", "A Fantasia", "12345678000199");
        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.doNothing().when(usuarioService).apagarUsuario(existente.getPatrocinadorId());

        patrocinadorService.apagarPatrocinador(1L);

        Mockito.verify(patrocinadorRepository).findById(1L);
        Mockito.verify(patrocinadorRepository).delete(existente);
        Mockito.verify(usuarioService).apagarUsuario(existente.getPatrocinadorId());

    }

    @Test
    public void deveLancarExcecaoSeCnpjExistir() {
        Patrocinador existente = new Patrocinador(1L, "Empresa A", "A Fantasia", "12345678000199");
        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.when(patrocinadorRepository.existsByCnpj("11111111000111"))
                .thenReturn(true);

        Assertions.assertAll(
                () -> assertThrows(CnpjJaCadastradoException.class, () ->
                        patrocinadorService.cadastrarPatrocinador(
                                "Empresa A",
                                "A Fantasia",
                                "11111111000111",
                                "teste@empresa.com",
                                "senha123",
                                "11 11111-1111")
                ),

                () -> assertThrows(CnpjJaCadastradoException.class, () ->
                        patrocinadorService.atualizarPatrocinador(
                                1L,
                                "Empresa X",
                                "X Fantasia",
                                "11111111000111",
                                "novo@empresa.com",
                                "senha123",
                                "11 55555-1111"
                        )
                )
        );

        Mockito.verify(patrocinadorRepository).findById(1L);
        Mockito.verify(patrocinadorRepository, Mockito.times(2)).existsByCnpj("11111111000111");
        Mockito.verifyNoMoreInteractions(usuarioService, patrocinadorRepository);
    }

    @Test
    public void deveLancarExcecaoSePatrocinadorNaoExistir() {
        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> patrocinadorService.getPatrocinadorById(1L)
        );

        Mockito.verify(patrocinadorRepository).findById(1L);
    }
}
