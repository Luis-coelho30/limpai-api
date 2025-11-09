package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.request.entity.AtualizarPatrocinadorRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador.PatrocinadorDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador.PatrocinadorMinDTO;
import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.PatrocinadorCadastroDTO;
import br.com.limpai.projeto_limpai.exception.user.CnpjJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.PatrocinadorRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
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
        p1.setUsuarioId(1L);
        p1.setRazaoSocial("Empresa A");
        p1.setNomeFantasia("A Fantasia");
        p1.setCnpj("12345678000199");

        Patrocinador p2 = new Patrocinador();
        p2.setUsuarioId(2L);
        p2.setRazaoSocial("Empresa B");
        p2.setNomeFantasia("B Fantasia");
        p2.setCnpj("98765432000188");

        List<Patrocinador> patrocinadoresFake = List.of(p1, p2);

        Mockito.when(patrocinadorRepository.findAll())
                .thenReturn(patrocinadoresFake);

        List<PatrocinadorMinDTO> resultado = patrocinadorService.listarPatrocinadores();

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
        p1.setUsuarioId(1L);
        p1.setRazaoSocial("Empresa A");
        p1.setNomeFantasia("A Fantasia");
        p1.setCnpj("12345678000199");

        Usuario usuario;
        usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setTelefone("11 11111-1111");
        usuario.setSenha("senha123");
        usuario.setTipo(UsuarioEnum.PATROCINADOR);

        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(p1));

        Mockito.when(usuarioService.getUsuarioPorId(1L))
                .thenReturn(usuario);

        PatrocinadorDTO patrocinadorDTO = patrocinadorService.getPatrocinadorById(1L);

        assertAll(
                () -> assertEquals("Empresa A", patrocinadorDTO.razaoSocial()),
                () -> assertEquals("A Fantasia", patrocinadorDTO.nomeFantasia()),
                () -> assertEquals("12345678000199", patrocinadorDTO.cnpj()),
                () -> assertEquals("teste@email.com", patrocinadorDTO.email()),
                () -> assertEquals("11 11111-1111", patrocinadorDTO.telefone())
        );

        Mockito.verify(patrocinadorRepository).findById(1L);
        Mockito.verify(usuarioService).getUsuarioPorId(1L);
    }

    @Test
    public void deveCadastrarPatrocinador() {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setEmail("teste@empresa.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("11 11111-1111");
        usuario.setTipo(UsuarioEnum.PATROCINADOR);

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
    public void deveAtualizarPatrocinadorParcialmente() {
        AtualizarPatrocinadorRequestDTO patrocinadorRequestDTO = new AtualizarPatrocinadorRequestDTO(
                "Empresa C",
                "A Fantasia",
                "12345678000199",
                "11 11111-1111"
        );

        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(new Patrocinador(1L,"A Fantasia", "Empresa A", "12345678000199")));

        Mockito.when(usuarioService.
                        atualizarTelefone(1L, "11 11111-1111"))
                .thenReturn(new Usuario(1L,"novoteste@empresa.com", "senha123", "11 11111-1111", UsuarioEnum.PATROCINADOR));

        Mockito.when(patrocinadorRepository.save(Mockito.any(Patrocinador.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PatrocinadorDTO patrocinadorDTO = patrocinadorService.atualizarParcial(1L, patrocinadorRequestDTO);

        assertAll(
                () -> assertEquals("Empresa C", patrocinadorDTO.nomeFantasia()),
                () -> assertEquals("A Fantasia", patrocinadorDTO.razaoSocial()),
                () -> assertEquals("12345678000199", patrocinadorDTO.cnpj()),
                () -> assertEquals("novoteste@empresa.com", patrocinadorDTO.email()),
                () -> assertEquals("11 11111-1111", patrocinadorDTO.telefone())
        );

        Mockito.verify(patrocinadorRepository).findById(1L);

        Mockito.verify(usuarioService).atualizarTelefone(
                1L,
                "11 11111-1111"
        );

        Mockito.verify(patrocinadorRepository).save(Mockito.any(Patrocinador.class));
    }

    @Test
    public void deveApagarPatrocinador() {
        Patrocinador existente = new Patrocinador(1L, "Empresa A", "A Fantasia", "12345678000199");
        Mockito.when(patrocinadorRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.doNothing().when(usuarioService).apagarUsuario(existente.getUsuarioId());

        patrocinadorService.apagarPatrocinador(1L);

        Mockito.verify(patrocinadorRepository).findById(1L);
        Mockito.verify(patrocinadorRepository).delete(existente);
        Mockito.verify(usuarioService).apagarUsuario(existente.getUsuarioId());

    }

    @Test
    public void deveLancarExcecaoSeCnpjExistirNoCadastro() {
        PatrocinadorCadastroDTO dto = new PatrocinadorCadastroDTO(
                "teste@empresa.com", "senha123", "11 11111-1111",
                "Empresa A", "A Fantasia", "11111111000111"
        );

        Mockito.when(patrocinadorRepository.existsByCnpj("11111111000111")).thenReturn(true);

        assertThrows(CnpjJaCadastradoException.class, () ->
                patrocinadorService.cadastrarPatrocinador(dto)
        );

        Mockito.verify(patrocinadorRepository).existsByCnpj("11111111000111");
        Mockito.verifyNoInteractions(usuarioService);
    }

    @Test
    public void deveLancarExcecaoSeCnpjExistirNaAtualizacao() {
        Patrocinador existente = new Patrocinador(1L, "Empresa A", "A Fantasia", "12345678000199");
        Usuario usuario = new Usuario(1L, "teste@email.com", "senha123", "11 11111-1111", UsuarioEnum.PATROCINADOR);

        PatrocinadorCadastroDTO dtoAtualizacao = new PatrocinadorCadastroDTO(
                "novo@empresa.com", "senha123", "11 55555-1111",
                "Empresa X", "X Fantasia", "11111111000111"
        );

        Mockito.when(patrocinadorRepository.findById(1L)).thenReturn(Optional.of(existente));

        Mockito.when(patrocinadorRepository.existsByCnpj("11111111000111")).thenReturn(true);

        assertThrows(CnpjJaCadastradoException.class, () ->
                patrocinadorService.atualizarPatrocinador(1L, dtoAtualizacao)
        );

        Mockito.verify(patrocinadorRepository).findById(1L);
        Mockito.verify(patrocinadorRepository).existsByCnpj("11111111000111");
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
