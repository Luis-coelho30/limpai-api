package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.PatrocinadorDTO;
import br.com.limpai.projeto_limpai.dto.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.cadastro.PatrocinadorCadastroDTO;
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

        List<PatrocinadorDTO> resultado = patrocinadorService.listarPatrocinadores();

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Empresa A", resultado.getFirst().razaoSocial()),
                () -> assertEquals("A Fantasia", resultado.getFirst().nomeFantasia()),

                () -> assertEquals("Empresa B", resultado.get(1).razaoSocial()),
                () -> assertEquals("B Fantasia", resultado.get(1).nomeFantasia())
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

        PatrocinadorDTO patrocinadorDTO = patrocinadorService.getPatrocinadorById(1L);

        assertAll(
                () -> assertEquals("Empresa A", patrocinadorDTO.razaoSocial()),
                () -> assertEquals("A Fantasia", patrocinadorDTO.nomeFantasia())
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

        PatrocinadorCadastroDTO patrocinadorDTO = new PatrocinadorCadastroDTO(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getTelefone(),
                "Empresa A",
                "A Fantasia",
                "12345678000199"
        );

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

        RegistroDTO registroDTO = patrocinadorService.cadastrarPatrocinador(patrocinadorDTO);

        assertAll(
                () -> assertEquals("A Fantasia", registroDTO.nome()),
                () -> assertEquals("teste@empresa.com", registroDTO.usuario().getEmail())
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
        PatrocinadorCadastroDTO patrocinadorAtualizadoDTO = new PatrocinadorCadastroDTO(
                "novoteste@empresa.com",
                "senha123",
                "11 55555-1111",
                "Empresa A",
                "C Fantasia",
                "12345678000199"
        );

        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(new Patrocinador(1L,"Empresa A", "A Fantasia", "12345678000199")));

        Mockito.when(usuarioService.
                        atualizarUsuario(1L, "novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.PATROCINADOR))
                .thenReturn(new Usuario(1L,"novoteste@empresa.com", "senha123", "11 55555-1111", UsuarioEnum.PATROCINADOR));

        Mockito.when(patrocinadorRepository.save(Mockito.any(Patrocinador.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegistroDTO registroDTO = patrocinadorService.atualizarPatrocinador(1L, patrocinadorAtualizadoDTO);

        assertAll(
                () -> assertEquals("C Fantasia", registroDTO.nome()),
                () -> assertEquals("novoteste@empresa.com", registroDTO.usuario().getEmail())
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

        PatrocinadorCadastroDTO patrocinadorDTO = new PatrocinadorCadastroDTO(
                "teste@empresa.com",
                "senha123",
                "11 11111-1111",
                "Empresa A",
                "A Fantasia",
                "11111111000111"
        );

        PatrocinadorCadastroDTO patrocinadorAtualizadoDTO = new PatrocinadorCadastroDTO(
                "novo@empresa.com",
                "senha123",
                "11 55555-1111",
                "Empresa X",
                "X Fantasia",
                "11111111000111"
        );

        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.when(patrocinadorRepository.existsByCnpj("11111111000111"))
                .thenReturn(true);

        Assertions.assertAll(
                () -> assertThrows(CnpjJaCadastradoException.class, () ->
                        patrocinadorService.cadastrarPatrocinador(
                                patrocinadorDTO
                        )
                ),

                () -> assertThrows(CnpjJaCadastradoException.class, () ->
                        patrocinadorService.atualizarPatrocinador(
                                1L,
                                patrocinadorAtualizadoDTO
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
