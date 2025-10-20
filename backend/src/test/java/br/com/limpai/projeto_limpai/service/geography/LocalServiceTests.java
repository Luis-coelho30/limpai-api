package br.com.limpai.projeto_limpai.service.geography;

import br.com.limpai.projeto_limpai.exception.geography.LocalJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.repository.geography.LocalRepository;
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
public class LocalServiceTests {

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private LocalService localService;

    @Test
    public void deveListarLocais() {
        Local l1 = new Local();
        l1.setLocalId(1L);
        l1.setNome("Praia Cristal");
        l1.setEndereco("Rua X");
        l1.setCep("11111111");
        l1.setCidadeId(1L);

        Local l2 = new Local();
        l2.setLocalId(2L);
        l2.setNome("Praia Suja");
        l2.setEndereco("Rua Y");
        l2.setCep("11111112");
        l2.setCidadeId(2L);

        List<Local> locaisFake = List.of(l1, l2);

        Mockito.when(localRepository.findAll())
                .thenReturn(locaisFake);

        List<Local> resultado = localService.listarLocais();

        assertAll(
                () -> assertEquals(2, resultado.size()),

                () -> assertEquals(1L, resultado.getFirst().getLocalId()),
                () -> assertEquals("Praia Cristal", resultado.getFirst().getNome()),
                () -> assertEquals("Rua X", resultado.getFirst().getEndereco()),
                () -> assertEquals("11111111", resultado.getFirst().getCep()),
                () -> assertEquals(1L, resultado.getFirst().getCidadeId()),

                () -> assertEquals(2L, resultado.get(1).getLocalId()),
                () -> assertEquals("Praia Suja", resultado.get(1).getNome()),
                () -> assertEquals("Rua Y", resultado.get(1).getEndereco()),
                () -> assertEquals("11111112", resultado.get(1).getCep()),
                () -> assertEquals(2L, resultado.get(1).getCidadeId())
        );
    }

    @Test
    public void deveListarLocalPorId() {
        Local p1 = new Local();
        p1.setLocalId(1L);
        p1.setNome("Praia Cristal");
        p1.setEndereco("Rua X");
        p1.setCep("11111111");
        p1.setCidadeId(1L);

        Mockito.when(localRepository.findById(1L))
                .thenReturn(Optional.of(p1));

        Local local = localService.getLocalById(1L);

        assertAll(
                () -> assertEquals(1L, local.getLocalId()),
                () -> assertEquals("Praia Cristal", local.getNome()),
                () -> assertEquals("Rua X", local.getEndereco()),
                () -> assertEquals("11111111", local.getCep()),
                () -> assertEquals(1L, local.getCidadeId())
        );
    }

    @Test
    public void deveRetornarTrueSeLocalExistir() {
        Mockito.when(localRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(localService.verificarLocalById(1L));

        Mockito.verify(localRepository).existsById(1L);
    }

    @Test
    public void deveCadastrarLocal() {
        Local localSalvo = new Local();
        localSalvo.setLocalId(1L);
        localSalvo.setNome("Praia Cristal");
        localSalvo.setEndereco("Rua X");
        localSalvo.setCep("11111111");
        localSalvo.setCidadeId(1L);

        Mockito.when(localRepository.save(Mockito.any(Local.class)))
                .thenReturn(localSalvo);

        Local resultado = localService.criarLocal(localSalvo.getNome(), localSalvo.getEndereco(),
                localSalvo.getCep(),localSalvo.getLocalId());

        assertAll(
                () -> assertEquals(1L, resultado.getLocalId()),
                () -> assertEquals("Praia Cristal", resultado.getNome()),
                () -> assertEquals("Rua X", resultado.getEndereco()),
                () -> assertEquals("11111111", resultado.getCep()),
                () -> assertEquals(1L, resultado.getCidadeId())
        );

        Mockito.verify(localRepository).save(Mockito.any(Local.class));
    }

    @Test
    public void deveAtualizarLocal() {
        Local localExistente = new Local();
        localExistente.setLocalId(1L);
        localExistente.setNome("Praia Cristal");
        localExistente.setEndereco("Rua X");
        localExistente.setCep("11111111");
        localExistente.setCidadeId(1L);

        Mockito.when(localRepository.findById(1L))
                .thenReturn(Optional.of(localExistente));

        Mockito.when(localRepository.save(Mockito.any(Local.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Local resultado = localService.atualizarLocal(1L, "Praia Suja", "Rua Y", "11111112", 1L);

        assertEquals(1L, resultado.getLocalId());
        assertEquals("Praia Suja", resultado.getNome());
        assertEquals("Rua Y", resultado.getEndereco());
        assertEquals("11111112", resultado.getCep());
        assertEquals(1L, resultado.getCidadeId());

        Mockito.verify(localRepository).findById(1L);
        Mockito.verify(localRepository).save(Mockito.any(Local.class));
    }

    @Test
    public void deveApagarLocal() {
        Local localExistente = new Local();
        localExistente.setLocalId(1L);
        localExistente.setNome("Praia Cristal");
        localExistente.setEndereco("Rua X");
        localExistente.setCep("11111111");
        localExistente.setCidadeId(1L);

        Mockito.when(localRepository.findById(1L))
                .thenReturn(Optional.of(localExistente));

        localService.apagarLocal(1L);

        Mockito.verify(localRepository).findById(1L);
        Mockito.verify(localRepository).delete(localExistente);
    }

    @Test
    public void deveLancarExcecaoSeLocalNaoExistir() {
        Mockito.when(localRepository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertAll(
                () -> assertThrows(LocalNaoEncontradoException.class,
                        () -> localService.getLocalById(1L)
                ),
                () -> assertThrows(LocalNaoEncontradoException.class,
                        () -> localService.atualizarLocal(1L, "Praia Limpa", "Rua X", "11111111", 1L)
                ),
                () -> assertThrows(LocalNaoEncontradoException.class,
                        () -> localService.apagarLocal(1L)
                )
        );

        Mockito.verify(localRepository, Mockito.times(3)).findById(1L);
    }

    @Test
    public void deveLancarExcecaoSeLocalJaFoiCadastrado() {
        Mockito.when(localRepository.findById(1L))
                .thenReturn(Optional.of(new Local(1L, "Praia dos Coqueiros", "Rua X, 123", "11111000", 1L)));

        Mockito.when(localRepository.existsByCepAndEndereco("11111000", "Rua X, 123"))
                .thenReturn(true);

        Assertions.assertAll(
                () -> assertThrows(LocalJaCadastradoException.class,
                        () -> localService.criarLocal("Praia Limpa dos Coqueiros", "Rua X, 123", "11111000", 1L)
                ),
                () -> assertThrows(LocalJaCadastradoException.class,
                        () -> localService.atualizarLocal(1L, "Praia dos Coqueiros", "Rua X, 123", "11111000", 1L)
                )
        );

        Mockito.verify(localRepository, Mockito.times(2)).existsByCepAndEndereco("11111000", "Rua X, 123");
        Mockito.verify(localRepository).findById(1L);
    }
}
