package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.dto.internal.CampanhaProjection;
import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.campanha.CampanhaMinDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.inscricao.MinhaInscricaoDTO;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.repository.geography.LocalRepository;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CampanhaRepositoryTests {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    @BeforeEach
    void setUp() {
        campanhaRepository.deleteAll();
        localRepository.deleteAll();
        patrocinadorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    public void deveSalvarCampanhaNova() {
        Long localId = criarLocalPadrao().getLocalId();
        Long usuarioId = criarUsuarioPadrao().getUsuarioId();
        criarPatrocinadorPadrao(usuarioId);

        Campanha campanha = criarCampanhaPadrao(
                localId,
                usuarioId,
                false
        );

        Campanha campanhaSalva = campanhaRepository.save(campanha);

        assertEquals(campanha.getNome(), campanhaSalva.getNome());
        assertEquals(campanha.getDescricao(), campanhaSalva.getDescricao());
        assertEquals(campanha.getLocalId(), campanhaSalva.getLocalId());
        assertEquals(campanha.getPatrocinadorId(), campanhaSalva.getPatrocinadorId());
        assertEquals(campanha.getDataInicio(), campanhaSalva.getDataInicio());
        assertEquals(campanha.getDataFim(), campanhaSalva.getDataFim());
        assertEquals(campanha.getMetaFundos(), campanhaSalva.getMetaFundos());
        assertEquals(campanha.getFundosArrecadados(), campanhaSalva.getFundosArrecadados());
    }

    @Test
    public void deveEncontrarCampanhaPorId() {
        Long localId = criarLocalPadrao().getLocalId();
        Long usuarioId = criarUsuarioPadrao().getUsuarioId();
        criarPatrocinadorPadrao(usuarioId);

        Campanha campanha = criarCampanhaPadrao(
                localId,
                usuarioId,
                false
        );

        Campanha campanhaSalva = campanhaRepository.save(campanha);

        Optional<Campanha> campanhaOpt = campanhaRepository.findById(campanhaSalva.getCampanhaId());
        assertTrue(campanhaOpt.isPresent(), "O patrocinador deveria ter sido encontrado no banco");
        Campanha campanhaEncontrada = campanhaOpt.get();

        assertEquals(campanhaSalva.getCampanhaId(), campanhaEncontrada.getCampanhaId());
        assertEquals(campanhaSalva.getNome(), campanhaEncontrada.getNome());
        assertEquals(campanhaSalva.getDescricao(), campanhaEncontrada.getDescricao());
        assertEquals(campanhaSalva.getLocalId(), campanhaEncontrada.getLocalId());
        assertEquals(campanhaSalva.getPatrocinadorId(), campanhaEncontrada.getPatrocinadorId());
        assertEquals(campanhaSalva.getDataInicio(), campanhaEncontrada.getDataInicio());
        assertEquals(campanhaSalva.getDataFim(), campanhaEncontrada.getDataFim());
        assertEquals(campanhaSalva.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaEncontrada.getMetaFundos());
        assertEquals(campanhaSalva.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaEncontrada.getFundosArrecadados());
    }

    @Test
    public void deveRetornarSeCampanhaExpirada() {
        Campanha campanha = salvarCampanha(false);

        assertFalse(campanhaRepository.isExpired(campanha.getCampanhaId()));
    }

    @Test
    public void deveRetornarSeCampanhaPertencePatrocinador() {
        Campanha campanha = salvarCampanha(false);

        assertTrue(campanhaRepository.isPatrocinadorDono(campanha.getCampanhaId(), campanha.getPatrocinadorId()));
    }

    @Test
    public void deveAdicionarFundosCampanha() {
        Campanha campanha = salvarCampanha(false);

        campanhaRepository.adicionarFundos(campanha.getCampanhaId(), BigDecimal.TEN);

        Optional<Campanha> campanhaOpt = campanhaRepository.findById(campanha.getCampanhaId());
        assertTrue(campanhaOpt.isPresent(), "O patrocinador deveria ter sido encontrado no banco");
        Campanha campanhaEncontrada = campanhaOpt.get();

        assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.UNNECESSARY), campanhaEncontrada.getFundosArrecadados());
    }

    @Test
    public void deveContarAsCampanhasExpiradas() {
        for (int i = 1; i<=10; i++) {
            salvarCampanha(true);
        }

        assertEquals(10, campanhaRepository.countCampanhasExpiradas());
    }

    @Test
    public void deveContarAsCampanhasNaoExpiradas() {
        for (int i = 1; i<=10; i++) {
            salvarCampanha(false);
        }

        assertEquals(10, campanhaRepository.countCampanhasNaoExpiradas());
    }

    @Test
    public void deveContarAsAtivasPorCidade() {
        for (int i = 1; i<=10; i++) {
            salvarCampanha(false);
        }

        assertEquals(10, campanhaRepository.countAtivasByCidade(1L));
    }

    @Test
    public void deveContarAsExpiradasPorCidade() {
        for (int i = 1; i<=10; i++) {
            salvarCampanha(true);
        }

        assertEquals(10, campanhaRepository.countExpiradasByCidade(1L));
    }

    @Test
    public void deveContarAsAtivasPorEstado() {
        for (int i = 1; i<=10; i++) {
            salvarCampanha(false);
        }

        assertEquals(10, campanhaRepository.countAtivasByEstado(1L));
    }

    @Test
    public void deveContarAsExpiradasPorEstado() {
        for (int i = 1; i<=10; i++) {
            salvarCampanha(true);
        }

        assertEquals(10, campanhaRepository.countExpiradasByEstado(1L));
    }

    @Test
    public void deveContarOsPatrocinadoresPorCampanha() {
        Campanha campanha = salvarCampanha(false);

        assertEquals(1, campanhaRepository.countByPatrocinadorId(campanha.getPatrocinadorId()));
    }

    @Test
    public void deveContarInscricoesPorUsuario() {
        Campanha campanha = salvarCampanha(false);

        assertEquals(0, campanhaRepository.countInscricoesByUsuario(campanha.getPatrocinadorId()));
    }

    @Test
    public void deveEncontrarCampanhaProjectionPorId() {
        Campanha campanha = salvarCampanha(false);
        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        Optional<CampanhaProjection> campanhaOpt = campanhaRepository.findCampanhaById(campanha.getCampanhaId());
        assertTrue(campanhaOpt.isPresent(), "O patrocinador deveria ter sido encontrado no banco");
        CampanhaProjection campanhaDTO = campanhaOpt.get();

        assertEquals(campanha.getCampanhaId(), campanhaDTO.campanhaId());
        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDescricao(), campanhaDTO.descricao());
        assertEquals(campanha.getDataInicio(), campanhaDTO.dataInicio());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.localId(), campanhaDTO.localId());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.endereco(), campanhaDTO.endereco());
        assertEquals(localDTO.cep(), campanhaDTO.cep());
        assertEquals(localDTO.cidadeId(), campanhaDTO.cidadeId());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveEncontrarCampanhaMinPorPatrocinador() {
        Campanha campanha = salvarCampanha(false);
        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findByPatrocinadorId(campanha.getPatrocinadorId(), Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveEncontrarInscricoesPorUsuario() {
        Campanha campanha = salvarCampanha(false);
        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<MinhaInscricaoDTO> inscricoes = campanhaRepository.findInscricoesByUsuario(campanha.getPatrocinadorId(), Pageable.unpaged());

        assertTrue(inscricoes.isEmpty());
    }

    @Test
    public void deveRetornarAsCampanhasExpiradas() {
        Campanha campanha = salvarCampanha(true);

        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findCampanhasExpiradas(Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveRetornarAsCampanhasAtivas() {
        Campanha campanha = salvarCampanha(false);

        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findCampanhasNaoExpiradas(Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveRetornarAsCampanhasExpiradasPorCidade() {
        Campanha campanha = salvarCampanha(true);

        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findExpiradasByCidade(localDTO.cidadeId(), Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveRetornarAsCampanhasAtivasPorCidade() {
        Campanha campanha = salvarCampanha(false);

        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findAtivasByCidade(localDTO.cidadeId(), Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveRetornarAsCampanhasExpiradasPorEstado() {
        Campanha campanha = salvarCampanha(true);

        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findExpiradasByEstado(1L, Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    @Test
    public void deveRetornarAsCampanhasAtivasPorEstado() {
        Campanha campanha = salvarCampanha(false);

        Optional<LocalResponseDTO> local = localRepository.findLocalById(campanha.getLocalId());

        Optional<LocalResponseDTO> localOpt = localRepository.findLocalById(campanha.getLocalId());
        assertTrue(localOpt.isPresent(), "O local deveria ter sido encontrado no banco");
        LocalResponseDTO localDTO = localOpt.get();

        List<CampanhaMinDTO> campanhaMinDTOList = campanhaRepository.findAtivasByCidade(1L, Pageable.unpaged());
        CampanhaMinDTO campanhaDTO = campanhaMinDTOList.getFirst();

        assertEquals(campanha.getNome(), campanhaDTO.nome());
        assertEquals(campanha.getDataFim(), campanhaDTO.dataFim());
        assertEquals(campanha.getMetaFundos().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.metaFundos());
        assertEquals(campanha.getFundosArrecadados().setScale(2, RoundingMode.UNNECESSARY), campanhaDTO.fundosArrecadados());
        assertEquals(0, campanhaDTO.qtdInscritos());
        assertEquals(localDTO.nome(), campanhaDTO.localNome());
        assertEquals(localDTO.cidadeNome(), campanhaDTO.cidadeNome());
        assertEquals(localDTO.estadoSigla(), campanhaDTO.estadoSigla());
    }

    private Campanha salvarCampanha(boolean expirada) {
        Long localId = criarLocalPadrao().getLocalId();
        Long usuarioId = criarUsuarioPadrao().getUsuarioId();
        criarPatrocinadorPadrao(usuarioId);

        Campanha campanha = criarCampanhaPadrao(
                localId,
                usuarioId,
                expirada
        );

        return campanhaRepository.save(campanha);
    }

    private Usuario criarUsuarioPadrao() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste" + System.nanoTime() + "@limpai.com");
        usuario.setSenha("123");
        usuario.setTelefone("11 92151-1511");
        usuario.setTipo(UsuarioEnum.PATROCINADOR);

        return usuarioRepository.save(usuario);
    }

    private void criarPatrocinadorPadrao(Long usuarioId) {
        Random random = new Random();
        Patrocinador patrocinador = new Patrocinador();
        patrocinador.setNomeFantasia("Limpai LTDA");
        patrocinador.setRazaoSocial("Limpai Empresa");
        patrocinador.setCnpj(gerarCnpjFake());

        patrocinadorRepository
                .insertPatrocinador(
                        usuarioId,
                        patrocinador.getRazaoSocial(),
                        patrocinador.getNomeFantasia(),
                        patrocinador.getCnpj()
                );
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

        return localRepository.save(local);
    }

    private Campanha criarCampanhaPadrao(Long localId, Long patrocinadorId, boolean expirada) {
        Campanha campanha = new Campanha();
        campanha.setPatrocinadorId(patrocinadorId);
        campanha.setLocalId(localId);
        campanha.setMetaFundos(BigDecimal.TEN);
        campanha.setNome("Campanha Teste");
        campanha.setDescricao("Descricao Teste");
        campanha.setDataInicio(LocalDateTime.now().minusDays(30).truncatedTo(ChronoUnit.SECONDS));

        if(expirada) {
            campanha.setDataFim(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        } else {
            campanha.setDataFim(LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS));
        }

        return campanha;
    }

    private String gerarCnpjFake() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
