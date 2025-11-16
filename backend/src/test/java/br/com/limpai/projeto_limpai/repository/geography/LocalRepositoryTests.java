package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;
import br.com.limpai.projeto_limpai.model.geography.Cidade;
import br.com.limpai.projeto_limpai.model.geography.Estado;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.repository.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class LocalRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired EstadoRepository estadoRepository;

    @BeforeEach
    void setUp() {
        localRepository.deleteAll();
    }

    @Test
    void deveSalvarNovoLocal() {
        Local local = criarLocalPadrao();

        Local localSalvo = localRepository.save(local);

        assertNotNull(localSalvo.getLocalId());
        assertEquals(local.getNome(), localSalvo.getNome());
        assertEquals(local.getEndereco(), localSalvo.getEndereco());
        assertEquals(local.getCep(), localSalvo.getCep());
        assertEquals(local.getCidadeId(), localSalvo.getCidadeId());
    }

    @Test
    void deveRetornarLocalPorId() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        Optional<Local> localOpt = localRepository.findById(localSalvo.getLocalId());

        assertTrue(localOpt.isPresent());
        Local localEncontrado = localOpt.get();

        assertEquals(localSalvo.getLocalId(), localEncontrado.getLocalId());
        assertEquals(localSalvo.getNome(), localEncontrado.getNome());
        assertEquals(localSalvo.getEndereco(), localEncontrado.getEndereco());
        assertEquals(localSalvo.getCep(), localEncontrado.getCep());
        assertEquals(localSalvo.getCidadeId(), localEncontrado.getCidadeId());
    }

    @Test
    void deveRetornarSeLocalExistePorCep_Endereco() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        assertTrue(localRepository.existsByCepAndEndereco(localSalvo.getCep(), localSalvo.getEndereco()));
    }

    @Test
    void deveContarPorCidade() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        assertEquals(1, localRepository.countByCidadeId(1L));
    }

    @Test
    void deveEncontrarLocalResponsePorId() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        Optional<LocalResponseDTO> localDTOOpt = localRepository.findLocalById(localSalvo.getLocalId());
        assertTrue(localDTOOpt.isPresent());
        LocalResponseDTO localEncontrado = localDTOOpt.get();

        Optional<Cidade> cidadeOpt = cidadeRepository.findById(1L);
        assertTrue(cidadeOpt.isPresent());
        Cidade cidadeEncontrada = cidadeOpt.get();

        Optional<Estado> estadoOpt = estadoRepository.findById(1L);
        assertTrue(estadoOpt.isPresent());
        Estado estadoEncontrado = estadoOpt.get();

        assertEquals(localSalvo.getLocalId(), localEncontrado.localId());
        assertEquals(localSalvo.getNome(), localEncontrado.nome());
        assertEquals(localSalvo.getEndereco(), localEncontrado.endereco());
        assertEquals(localSalvo.getCep(), localEncontrado.cep());
        assertEquals(localSalvo.getCidadeId(), localEncontrado.cidadeId());
        assertEquals(cidadeEncontrada.cidadeId(), localEncontrado.cidadeId());
        assertEquals(estadoEncontrado.sigla(), localEncontrado.estadoSigla());
    }

    @Test
    void deveEncontrarTodosLocaisResponse() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        List<LocalResponseDTO> localResponseDTOS = localRepository.findAllLocal(Pageable.unpaged());
        assertFalse(localResponseDTOS.isEmpty());
        LocalResponseDTO localEncontrado = localResponseDTOS.getFirst();

        Optional<Cidade> cidadeOpt = cidadeRepository.findById(1L);
        assertTrue(cidadeOpt.isPresent());
        Cidade cidadeEncontrada = cidadeOpt.get();

        Optional<Estado> estadoOpt = estadoRepository.findById(1L);
        assertTrue(estadoOpt.isPresent());
        Estado estadoEncontrado = estadoOpt.get();

        assertEquals(localSalvo.getLocalId(), localEncontrado.localId());
        assertEquals(localSalvo.getNome(), localEncontrado.nome());
        assertEquals(localSalvo.getEndereco(), localEncontrado.endereco());
        assertEquals(localSalvo.getCep(), localEncontrado.cep());
        assertEquals(localSalvo.getCidadeId(), localEncontrado.cidadeId());
        assertEquals(cidadeEncontrada.cidadeId(), localEncontrado.cidadeId());
        assertEquals(estadoEncontrado.sigla(), localEncontrado.estadoSigla());
    }

    @Test
    void deveEncontrarLocaisResponsePorCidade() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        List<LocalResponseDTO> localResponseDTOS = localRepository.findLocalByCidade(1L, Pageable.unpaged());
        assertFalse(localResponseDTOS.isEmpty());
        LocalResponseDTO localEncontrado = localResponseDTOS.getFirst();

        Optional<Cidade> cidadeOpt = cidadeRepository.findById(1L);
        assertTrue(cidadeOpt.isPresent());
        Cidade cidadeEncontrada = cidadeOpt.get();

        Optional<Estado> estadoOpt = estadoRepository.findById(1L);
        assertTrue(estadoOpt.isPresent());
        Estado estadoEncontrado = estadoOpt.get();

        assertEquals(localSalvo.getLocalId(), localEncontrado.localId());
        assertEquals(localSalvo.getNome(), localEncontrado.nome());
        assertEquals(localSalvo.getEndereco(), localEncontrado.endereco());
        assertEquals(localSalvo.getCep(), localEncontrado.cep());
        assertEquals(localSalvo.getCidadeId(), localEncontrado.cidadeId());
        assertEquals(cidadeEncontrada.cidadeId(), localEncontrado.cidadeId());
        assertEquals(estadoEncontrado.sigla(), localEncontrado.estadoSigla());
    }

    @Test
    void deveApagarLocal() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        Optional<Local> localOpt = localRepository.findById(localSalvo.getLocalId());

        assertTrue(localOpt.isPresent());
        Local localEncontrado = localOpt.get();

        localRepository.delete(localEncontrado);
    }

    @Test
    void deveApagarLocalPorId() {
        Local localSalvo = localRepository.save(criarLocalPadrao());

        Optional<Local> localOpt = localRepository.findById(localSalvo.getLocalId());

        assertTrue(localOpt.isPresent());
        Local localEncontrado = localOpt.get();

        localRepository.deleteById(localEncontrado.getLocalId());
    }

    private Local criarLocalPadrao() {
        Random random = new Random();
        Local local = new Local();
        String sufixoUnico = UUID.randomUUID().toString().substring(0, 8);
        int cepRandom = 10000000 + random.nextInt(90000000);

        local.setNome("Local " + sufixoUnico);
        local.setCep(String.valueOf(cepRandom));
        local.setEndereco("Rua Teste " + sufixoUnico + ", " + random.nextInt(1000));
        local.setCidadeId(1L);

        return local;
    }
}
