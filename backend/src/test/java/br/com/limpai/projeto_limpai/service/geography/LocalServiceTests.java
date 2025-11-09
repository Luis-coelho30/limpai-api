package br.com.limpai.projeto_limpai.service.geography;

import br.com.limpai.projeto_limpai.dto.request.cadastro.LocalRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;
import br.com.limpai.projeto_limpai.exception.geography.CidadeNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.geography.LocalJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.repository.geography.CidadeRepository;
import br.com.limpai.projeto_limpai.repository.geography.LocalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalServiceTests {

    @Mock
    private LocalRepository localRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private LocalService localService;

    private final Long LOCAL_ID = 1L;
    private final Long CIDADE_ID = 10L;
    private final LocalRequestDTO REQUEST_DTO = new LocalRequestDTO("Parque", "Rua A", "12345678", CIDADE_ID);
    private final LocalResponseDTO VIEW_COMPLETA = new LocalResponseDTO(
            LOCAL_ID, "Parque", "Rua A", "12345678", CIDADE_ID, "São Paulo", "SP"
    );
    private final Local ENTIDADE_LOCAL = new Local(LOCAL_ID, "Parque", "Rua A", "12345678", CIDADE_ID);

    @Nested
    @DisplayName("Listar Locais")
    class ListarLocais {

        @Test
        @DisplayName("Deve listar todos os locais quando sem filtro")
        void deveListarTodosSemFiltro() {
            Pageable pageable = PageRequest.of(0, 10);
            when(localRepository.findAllLocal(pageable)).thenReturn(List.of(VIEW_COMPLETA));
            when(localRepository.count()).thenReturn(1L);

            Page<LocalResponseDTO> resultado = localService.listarLocais(null, pageable);

            assertEquals(1, resultado.getTotalElements());
            assertEquals("Parque", resultado.getContent().getFirst().nome());
            verify(localRepository).findAllLocal(pageable);
            verify(localRepository, never()).findLocalByCidade(any(), any());
        }

        @Test
        @DisplayName("Deve listar filtrando por cidade quando filtro presente")
        void deveListarComFiltroCidade() {
            Pageable pageable = PageRequest.of(0, 10);
            when(localRepository.findLocalByCidade(CIDADE_ID, pageable)).thenReturn(List.of(VIEW_COMPLETA));
            when(localRepository.countByCidadeId(CIDADE_ID)).thenReturn(1L);

            Page<LocalResponseDTO> resultado = localService.listarLocais(CIDADE_ID, pageable);

            assertEquals(1, resultado.getTotalElements());
            verify(localRepository).findLocalByCidade(CIDADE_ID, pageable);
            verify(localRepository, never()).findAllLocal(any());
        }
    }

    @Nested
    @DisplayName("Buscar por ID")
    class BuscarPorId {

        @Test
        @DisplayName("Deve retornar DTO quando encontrado")
        void deveRetornarDtoQuandoEncontrado() {
            when(localRepository.findLocalById(LOCAL_ID)).thenReturn(Optional.of(VIEW_COMPLETA));

            LocalResponseDTO resultado = localService.buscarPorId(LOCAL_ID);

            assertNotNull(resultado);
            assertEquals(LOCAL_ID, resultado.id());
            assertEquals("São Paulo", resultado.cidadeNome());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não encontrado")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            when(localRepository.findLocalById(LOCAL_ID)).thenReturn(Optional.empty());

            assertThrows(LocalNaoEncontradoException.class, () -> localService.buscarPorId(LOCAL_ID));
        }
    }

    @Nested
    @DisplayName("Criar Local")
    class CriarLocal {

        @Test
        @DisplayName("Deve criar local com sucesso")
        void deveCriarLocalComSucesso() {
            when(cidadeRepository.existsById(CIDADE_ID)).thenReturn(true);
            when(localRepository.existsByCepAndEndereco(any(), any())).thenReturn(false);
            when(localRepository.save(any(Local.class))).thenReturn(ENTIDADE_LOCAL);
            when(localRepository.findLocalById(LOCAL_ID)).thenReturn(Optional.of(VIEW_COMPLETA));

            LocalResponseDTO resultado = localService.criarLocal(REQUEST_DTO);

            assertNotNull(resultado);
            assertEquals(LOCAL_ID, resultado.id());
            verify(localRepository).save(any(Local.class));
        }

        @Test
        @DisplayName("Deve lançar exceção se cidade não existir")
        void deveFalharCidadeInexistente() {
            when(cidadeRepository.existsById(CIDADE_ID)).thenReturn(false);

            assertThrows(CidadeNaoEncontradaException.class, () -> localService.criarLocal(REQUEST_DTO));
            verify(localRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção se local já cadastrado")
        void deveFalharLocalDuplicado() {
            when(cidadeRepository.existsById(CIDADE_ID)).thenReturn(true);
            when(localRepository.existsByCepAndEndereco(REQUEST_DTO.cep(), REQUEST_DTO.endereco())).thenReturn(true);

            assertThrows(LocalJaCadastradoException.class, () -> localService.criarLocal(REQUEST_DTO));
            verify(localRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Atualizar Local")
    class AtualizarLocal {

        @Test
        @DisplayName("Deve atualizar sem validar duplicidade se endereço não mudou")
        void deveAtualizarSemValidarSeEnderecoIgual() {
            when(localRepository.findById(LOCAL_ID)).thenReturn(Optional.of(ENTIDADE_LOCAL));
            when(localRepository.save(any(Local.class))).thenReturn(ENTIDADE_LOCAL);
            when(localRepository.findLocalById(LOCAL_ID)).thenReturn(Optional.of(VIEW_COMPLETA));

            localService.atualizarLocal(LOCAL_ID, REQUEST_DTO);

            verify(localRepository, never()).existsByCepAndEndereco(any(), any());
            verify(localRepository).save(any(Local.class));
        }

        @Test
        @DisplayName("Deve validar duplicidade se endereço mudou")
        void deveValidarSeEnderecoMudou() {
            Local localAntigo = new Local(LOCAL_ID, "Parque Antigo", "Rua Velha", "00000000", CIDADE_ID);
            when(localRepository.findById(LOCAL_ID)).thenReturn(Optional.of(localAntigo));

            when(localRepository.existsByCepAndEndereco(REQUEST_DTO.cep(), REQUEST_DTO.endereco())).thenReturn(true);

            assertThrows(LocalJaCadastradoException.class, () -> localService.atualizarLocal(LOCAL_ID, REQUEST_DTO));
            verify(localRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Apagar Local")
    class ApagarLocal {
        @Test
        @DisplayName("Deve apagar se existir")
        void deveApagarSeExistir() {
            when(localRepository.existsById(LOCAL_ID)).thenReturn(true);
            localService.apagarLocal(LOCAL_ID);
            verify(localRepository).deleteById(LOCAL_ID);
        }

        @Test
        @DisplayName("Deve lançar exceção se não existir ao tentar apagar")
        void deveFalharSeNaoExistir() {
            when(localRepository.existsById(LOCAL_ID)).thenReturn(false);
            assertThrows(LocalNaoEncontradoException.class, () -> localService.apagarLocal(LOCAL_ID));
            verify(localRepository, never()).deleteById(any());
        }
    }
}
