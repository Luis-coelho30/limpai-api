package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.request.entity.CriarCampanhaDTO;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import br.com.limpai.projeto_limpai.repository.entity.CampanhaRepository;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.service.geography.LocalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CampanhaServiceTests {

    @Mock
    private CampanhaRepository campanhaRepository;

    @Mock
    private UsuarioCampanhaRepository inscricaoRepository;

    @Mock
    private LocalService localService;

    @InjectMocks
    private CampanhaService campanhaService;

    @Test
    public void deveRetornarTrueSeCampanhaExpirou() {
        Mockito.when(campanhaRepository.isExpired(1L))
                .thenReturn(true);

        assertTrue(campanhaService.verificarCampanhaExpirada(1L));

        Mockito.verify(campanhaRepository).isExpired(1L);
    }

    @Test
    public void deveRetornarTrueSeCampanhaExistir() {
        Mockito.when(campanhaRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(campanhaService.verificarCampanhaPorId(1L));

        Mockito.verify(campanhaRepository).existsById(1L);
    }

    @Test
    public void deveListarCampanhas() {
        Campanha c1 = new Campanha();
        c1.setCampanhaId(1L);
        c1.setNome("Limpeza da Praia Cristal");
        c1.setDescricao("Bora limpar!");
        c1.setDataInicio(LocalDateTime.MIN);
        c1.setDataFim(LocalDateTime.MAX);
        c1.setLocalId(1L);

        Campanha c2 = new Campanha();
        c2.setCampanhaId(2L);
        c2.setNome("Limpeza da Praia Coral");
        c2.setDescricao("Bora limpar!");
        c2.setDataInicio(LocalDateTime.MIN);
        c2.setDataFim(LocalDateTime.MAX);
        c2.setLocalId(2L);

        Mockito.when(campanhaRepository.findAll())
                .thenReturn(List.of(c1, c2));

        List<Campanha> resultado = campanhaService.listarCampanhas();

        assertAll(
                () -> assertEquals(2, resultado.size()),

                () -> assertEquals(1L, resultado.getFirst().getCampanhaId()),
                () -> assertEquals("Limpeza da Praia Cristal", resultado.getFirst().getNome()),
                () -> assertEquals("Bora limpar!", resultado.getFirst().getDescricao()),
                () -> assertEquals(LocalDateTime.MIN, resultado.getFirst().getDataInicio()),
                () -> assertEquals(LocalDateTime.MAX, resultado.getFirst().getDataFim()),
                () -> assertEquals(1L, resultado.getFirst().getLocalId()),

                () -> assertEquals(2L, resultado.get(1).getCampanhaId()),
                () -> assertEquals("Limpeza da Praia Coral", resultado.get(1).getNome()),
                () -> assertEquals("Bora limpar!", resultado.get(1).getDescricao()),
                () -> assertEquals(LocalDateTime.MIN, resultado.get(1).getDataInicio()),
                () -> assertEquals(LocalDateTime.MAX, resultado.get(1).getDataFim()),
                () -> assertEquals(2L, resultado.get(1).getLocalId())
        );

        Mockito.verify(campanhaRepository).findAll();
    }

    @Test
    public void deveListarCampanhaPorId() {
        Campanha campanhaSalva = new Campanha();
        campanhaSalva.setCampanhaId(1L);
        campanhaSalva.setNome("Limpeza da Praia Cristal");
        campanhaSalva.setDescricao("Bora limpar!");
        campanhaSalva.setDataInicio(LocalDateTime.MIN);
        campanhaSalva.setDataFim(LocalDateTime.MAX);
        campanhaSalva.setLocalId(1L);

        Mockito.when(campanhaRepository.findById(1L))
                .thenReturn(Optional.of(campanhaSalva));

        Campanha resultado = campanhaService.getCampanhaById(1L);

        assertAll(
                () -> assertEquals(1L, resultado.getCampanhaId()),
                () -> assertEquals("Limpeza da Praia Cristal", resultado.getNome()),
                () -> assertEquals("Bora limpar!", resultado.getDescricao()),
                () -> assertEquals(LocalDateTime.MIN, resultado.getDataInicio()),
                () -> assertEquals(LocalDateTime.MAX, resultado.getDataFim()),
                () -> assertEquals(1L, resultado.getLocalId())
        );

        Mockito.verify(campanhaRepository).findById(1L);
    }

    @Test
    public void deveCadastrarCampanha() {
        CriarCampanhaDTO campanhaDTO = new CriarCampanhaDTO("Limpeza da Praia Cristal", "Bora limpar!", LocalDateTime.MIN,
                LocalDateTime.MAX, BigDecimal.ZERO, 1L);

        Campanha campanhaSalva = new Campanha();
        campanhaSalva.setCampanhaId(1L);
        campanhaSalva.setNome("Limpeza da Praia Cristal");
        campanhaSalva.setDescricao("Bora limpar!");
        campanhaSalva.setDataInicio(LocalDateTime.MIN);
        campanhaSalva.setDataFim(LocalDateTime.MAX);
        campanhaSalva.setLocalId(1L);

        Mockito.when(campanhaRepository.save(Mockito.any(Campanha.class)))
                .thenReturn(campanhaSalva);

        Mockito.when(localService.verificarLocalById(1L))
                .thenReturn(true);

        Campanha resultado = campanhaService.criarCampanha(2L, campanhaDTO);

        assertAll(
                () -> assertEquals(1L, resultado.getCampanhaId()),
                () -> assertEquals("Limpeza da Praia Cristal", resultado.getNome()),
                () -> assertEquals("Bora limpar!", resultado.getDescricao()),
                () -> assertEquals(LocalDateTime.MIN, resultado.getDataInicio()),
                () -> assertEquals(LocalDateTime.MAX, resultado.getDataFim()),
                () -> assertEquals(1L, resultado.getLocalId())
        );

        Mockito.verify(campanhaRepository).save(Mockito.any(Campanha.class));
        Mockito.verify(localService).verificarLocalById(1L);
    }

    @Test
    public void deveAtualizarCampanha() {
        CriarCampanhaDTO campanhaDTO = new CriarCampanhaDTO("Lixo na Praia Cristal", "Bora sujar!", LocalDateTime.MAX,
                LocalDateTime.MIN, BigDecimal.TEN, 1L);

        Campanha campanhaExistente = new Campanha();
        campanhaExistente.setCampanhaId(1L);
        campanhaExistente.setNome("Limpeza da Praia Cristal");
        campanhaExistente.setDescricao("Bora limpar!");
        campanhaExistente.setDataInicio(LocalDateTime.MIN);
        campanhaExistente.setDataFim(LocalDateTime.MAX);
        campanhaExistente.setLocalId(1L);

        Mockito.when(campanhaRepository.findById(1L))
                .thenReturn(Optional.of(campanhaExistente));

        Mockito.when(campanhaRepository.save(Mockito.any(Campanha.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Campanha resultado = campanhaService.atualizarCampanha(1L, campanhaDTO);

        assertAll(
                () -> assertEquals(1L, resultado.getCampanhaId()),
                () -> assertEquals("Lixo na Praia Cristal", resultado.getNome()),
                () -> assertEquals("Bora sujar!", resultado.getDescricao()),
                () -> assertEquals(LocalDateTime.MAX, resultado.getDataInicio()),
                () -> assertEquals(LocalDateTime.MIN, resultado.getDataFim()),
                () -> assertEquals(1L, resultado.getLocalId())
        );

        Mockito.verify(campanhaRepository).findById(1L);
        Mockito.verify(campanhaRepository).save(Mockito.any(Campanha.class));
    }

    @Test
    public void deveExcluirCampanha() {
        Campanha campanhaExistente = new Campanha();
        campanhaExistente.setCampanhaId(1L);
        campanhaExistente.setNome("Limpeza da Praia Cristal");
        campanhaExistente.setDescricao("Bora limpar!");
        campanhaExistente.setDataInicio(LocalDateTime.MIN);
        campanhaExistente.setDataFim(LocalDateTime.MAX);
        campanhaExistente.setLocalId(1L);

        Mockito.when(campanhaRepository.findById(1L))
                .thenReturn(Optional.of(campanhaExistente));

        Mockito.doNothing()
                .when(inscricaoRepository)
                .removerHistoricoDeInscricoesByCampanha(1L);

        campanhaService.apagarCampanha(1L);

        Mockito.verify(campanhaRepository).findById(1L);
        Mockito.verify(inscricaoRepository).removerHistoricoDeInscricoesByCampanha(1L);
        Mockito.verify(campanhaRepository).delete(campanhaExistente);
    }

    @Test
    public void deveLancarExcecaoSeCampanhaNaoExistir() {
        CriarCampanhaDTO campanhaDTO = new CriarCampanhaDTO("Limpeza da Praia Cristal", "Bora limpar!", LocalDateTime.MIN,
                LocalDateTime.MAX, BigDecimal.ZERO, 1L);

        Mockito.when(campanhaRepository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertAll(
                () -> assertThrows(CampanhaNaoEncontradaException.class,
                        () -> campanhaService.getCampanhaById(1L)
                ),
                () -> assertThrows(CampanhaNaoEncontradaException.class,
                        () -> campanhaService.atualizarCampanha(1L, campanhaDTO)
                ),
                () -> assertThrows(CampanhaNaoEncontradaException.class,
                        () -> campanhaService.apagarCampanha(1L)
                )
        );

        Mockito.verify(campanhaRepository, Mockito.times(3)).findById(1L);
    }

    @Test
    public void deveLancarExcecaoSeLocalNaoExistir() {
        CriarCampanhaDTO campanhaDTO = new CriarCampanhaDTO("Limpeza da Praia Cristal", "Bora limpar!", LocalDateTime.MIN,
                LocalDateTime.MAX, BigDecimal.ZERO, 1L);

        Campanha campanhaExistente = new Campanha();
        campanhaExistente.setCampanhaId(1L);

        Mockito.when(campanhaRepository.findById(1L))
                .thenReturn(Optional.of(campanhaExistente));

        Mockito.when(localService.verificarLocalById(1L))
                .thenReturn(false);

        assertThrows(LocalNaoEncontradoException.class, () ->
                campanhaService.criarCampanha(2L, campanhaDTO)
        );

        assertThrows(LocalNaoEncontradoException.class, () ->
                campanhaService.atualizarCampanha(1L, campanhaDTO)
        );

        Mockito.verify(campanhaRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(localService, Mockito.times(2)).verificarLocalById(1L);
    }

}
