package br.com.limpai.projeto_limpai.service.join;

import br.com.limpai.projeto_limpai.exception.campanha.CampanhaExpiradaException;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioJaEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioNaoEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.join.UsuarioCampanha;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.service.entity.CampanhaService;
import br.com.limpai.projeto_limpai.service.entity.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InscricaoServiceTest {

    @Mock
    UsuarioCampanhaRepository inscricaoRepository;

    @Mock
    CampanhaService campanhaService;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    InscricaoService inscricaoService;

    @Test
    public void deveListarInscricoesPorUsuario() {
        UsuarioCampanha i1 = new UsuarioCampanha();
        i1.setUsuarioId(1L);
        i1.setCampanhaId(1L);
        i1.setDataInscricao(LocalDateTime.MAX);

        UsuarioCampanha i2 = new UsuarioCampanha();
        i2.setUsuarioId(1L);
        i2.setCampanhaId(2L);
        i2.setDataInscricao(LocalDateTime.MAX);

        Mockito.when(inscricaoRepository.findAllByUsuario(1L))
                .thenReturn(List.of(i1, i2));

        List<UsuarioCampanha> inscricaoList = inscricaoService.getAllByUsuario(1L);

        Assertions.assertAll(
                () -> assertEquals(2, inscricaoList.size()),

                () -> assertEquals(1L, inscricaoList.getFirst().getUsuarioId()),
                () -> assertEquals(1L, inscricaoList.getFirst().getCampanhaId()),
                () -> assertEquals(LocalDateTime.MAX, inscricaoList.getFirst().getDataInscricao()),

                () -> assertEquals(1L, inscricaoList.get(1).getUsuarioId()),
                () -> assertEquals(2L, inscricaoList.get(1).getCampanhaId()),
                () -> assertEquals(LocalDateTime.MAX, inscricaoList.get(1).getDataInscricao())
        );

        Mockito.verify(inscricaoRepository).findAllByUsuario(1L);
    }

    @Test
    public void deveListarInscricoesPorCampanha() {
        UsuarioCampanha i1 = new UsuarioCampanha();
        i1.setUsuarioId(1L);
        i1.setCampanhaId(1L);
        i1.setDataInscricao(LocalDateTime.MAX);

        UsuarioCampanha i2 = new UsuarioCampanha();
        i2.setUsuarioId(2L);
        i2.setCampanhaId(1L);
        i2.setDataInscricao(LocalDateTime.MAX);

        Mockito.when(inscricaoRepository.findAllByCampanha(1L))
                .thenReturn(List.of(i1, i2));

        List<UsuarioCampanha> inscricaoList = inscricaoService.getAllByCampanha(1L);

        Assertions.assertAll(
                () -> assertEquals(2, inscricaoList.size()),

                () -> assertEquals(1L, inscricaoList.getFirst().getUsuarioId()),
                () -> assertEquals(1L, inscricaoList.getFirst().getCampanhaId()),
                () -> assertEquals(LocalDateTime.MAX, inscricaoList.getFirst().getDataInscricao()),

                () -> assertEquals(2L, inscricaoList.get(1).getUsuarioId()),
                () -> assertEquals(1L, inscricaoList.get(1).getCampanhaId()),
                () -> assertEquals(LocalDateTime.MAX, inscricaoList.get(1).getDataInscricao())
        );

        Mockito.verify(inscricaoRepository).findAllByCampanha(1L);
    }

    @Test
    public void deveInscreverEmCampanha() {
        Long usuarioId = 1L;
        Long campanhaId = 10L;

        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId))
                .thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId))
                .thenReturn(true);
        Mockito.when(inscricaoRepository.existsByUsuarioAndCampanha(usuarioId, campanhaId))
                .thenReturn(false);

        Mockito.doNothing()
                .when(inscricaoRepository)
                .inscrever(Mockito.any(UsuarioCampanha.class));

        inscricaoService.inscrever(usuarioId, campanhaId);

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verify(inscricaoRepository).existsByUsuarioAndCampanha(usuarioId, campanhaId);
        Mockito.verify(inscricaoRepository).inscrever(Mockito.any(UsuarioCampanha.class));
    }

    @Test
    public void deveCancelarInscricaoEmCampanha() {
        Long usuarioId = 1L;
        Long campanhaId = 10L;

        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId))
                .thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId))
                .thenReturn(true);
        Mockito.when(inscricaoRepository.existsByUsuarioAndCampanha(usuarioId, campanhaId))
                .thenReturn(true);

        Mockito.doNothing()
                .when(inscricaoRepository)
                .removerInscricao(usuarioId, campanhaId);

        inscricaoService.desinscrever(usuarioId, campanhaId);

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verify(inscricaoRepository).existsByUsuarioAndCampanha(usuarioId, campanhaId);
        Mockito.verify(inscricaoRepository).removerInscricao(usuarioId, campanhaId);
    }

    @Test
    void inscrever_deveLancarExcecaoSeUsuarioNaoExiste() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(false);

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                inscricaoService.inscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verifyNoMoreInteractions(campanhaService, inscricaoRepository);
    }

    @Test
    void inscrever_deveLancarExcecaoSeCampanhaNaoExiste() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId)).thenReturn(false);

        assertThrows(CampanhaNaoEncontradaException.class, () ->
                inscricaoService.inscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verifyNoMoreInteractions(inscricaoRepository);
    }

    @Test
    void inscrever_deveLancarExcecaoSeCampanhaExpirada() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaExpirada(campanhaId)).thenReturn(true);

        assertThrows(CampanhaExpiradaException.class, () ->
                inscricaoService.inscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verify(campanhaService).verificarCampanhaExpirada(campanhaId);
        Mockito.verifyNoMoreInteractions(inscricaoRepository);
    }

    @Test
    void inscrever_deveLancarExcecaoSeUsuarioJaInscrito() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaExpirada(campanhaId)).thenReturn(false);
        Mockito.when(inscricaoRepository.existsByUsuarioAndCampanha(usuarioId, campanhaId)).thenReturn(true);

        assertThrows(UsuarioJaEstaInscritoException.class, () ->
                inscricaoService.inscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verify(campanhaService).verificarCampanhaExpirada(campanhaId);
        Mockito.verify(inscricaoRepository).existsByUsuarioAndCampanha(usuarioId, campanhaId);
        Mockito.verify(inscricaoRepository, Mockito.never()).inscrever(Mockito.any());
    }

    @Test
    void desinscrever_deveLancarExcecaoSeUsuarioNaoExiste() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(false);

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                inscricaoService.desinscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verifyNoMoreInteractions(campanhaService, inscricaoRepository);
    }

    @Test
    void desinscrever_deveLancarExcecaoSeCampanhaNaoExiste() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId)).thenReturn(false);

        assertThrows(CampanhaNaoEncontradaException.class, () ->
                inscricaoService.desinscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verifyNoMoreInteractions(inscricaoRepository);
    }

    @Test
    void desinscrever_deveLancarExcecaoSeCampanhaExpirada() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaExpirada(campanhaId)).thenReturn(true);

        assertThrows(CampanhaExpiradaException.class, () ->
                inscricaoService.desinscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verify(campanhaService).verificarCampanhaExpirada(campanhaId);
        Mockito.verifyNoMoreInteractions(inscricaoRepository);
    }

    @Test
    void desinscrever_deveLancarExcecaoSeInscricaoNaoExiste() {
        Long usuarioId = 1L, campanhaId = 2L;
        Mockito.when(usuarioService.verificarUsuarioPorId(usuarioId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaPorId(campanhaId)).thenReturn(true);
        Mockito.when(campanhaService.verificarCampanhaExpirada(campanhaId)).thenReturn(false);
        Mockito.when(inscricaoRepository.existsByUsuarioAndCampanha(usuarioId, campanhaId)).thenReturn(false);

        assertThrows(UsuarioNaoEstaInscritoException.class, () ->
                inscricaoService.desinscrever(usuarioId, campanhaId)
        );

        Mockito.verify(usuarioService).verificarUsuarioPorId(usuarioId);
        Mockito.verify(campanhaService).verificarCampanhaPorId(campanhaId);
        Mockito.verify(campanhaService).verificarCampanhaExpirada(campanhaId);
        Mockito.verify(inscricaoRepository).existsByUsuarioAndCampanha(usuarioId, campanhaId);
        Mockito.verify(inscricaoRepository, Mockito.never()).removerInscricao(usuarioId, campanhaId);
    }
}
