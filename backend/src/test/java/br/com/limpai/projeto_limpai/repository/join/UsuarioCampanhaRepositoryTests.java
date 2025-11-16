package br.com.limpai.projeto_limpai.repository.join;

import br.com.limpai.projeto_limpai.model.entity.Campanha;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.model.join.UsuarioCampanha;
import br.com.limpai.projeto_limpai.repository.AbstractIntegrationTest;
import br.com.limpai.projeto_limpai.repository.entity.CampanhaRepository;
import br.com.limpai.projeto_limpai.repository.entity.PatrocinadorRepository;
import br.com.limpai.projeto_limpai.repository.entity.UsuarioRepository;
import br.com.limpai.projeto_limpai.repository.entity.VoluntarioRepository;
import br.com.limpai.projeto_limpai.repository.geography.LocalRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UsuarioCampanhaRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private UsuarioCampanhaRepository inscricaoRepository;

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LocalRepository localRepository;

    @BeforeEach
    void setUp() {
        inscricaoRepository.deleteAll();
        localRepository.deleteAll();
        campanhaRepository.deleteAll();
        voluntarioRepository.deleteAll();
        patrocinadorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveSalvarNovaInscricao() {
        UsuarioCampanha inscricao = criarCenarioInscricao();

        inscricaoRepository.inscrever(inscricao);

        List<UsuarioCampanha> inscricoes = inscricaoRepository.findAllByUsuario(inscricao.getUsuarioId());
        assertFalse(inscricoes.isEmpty(), "O usuário deveria estar inscrito nessa campanha.");

        UsuarioCampanha inscricaoEncontrada = inscricoes.getFirst();

        assertEquals(inscricao.getCampanhaId(), inscricaoEncontrada.getCampanhaId());
        assertEquals(inscricao.getUsuarioId(), inscricaoEncontrada.getUsuarioId());
        assertEquals(inscricao.getDataInscricao(), inscricaoEncontrada.getDataInscricao());
    }

    @Test
    void deveRetornarSeInscricaoExistePorUsuario_Campanha() {
        UsuarioCampanha inscricao = criarCenarioInscricao();

        inscricaoRepository.inscrever(inscricao);

        assertTrue(inscricaoRepository.existsByUsuarioAndCampanha(inscricao.getUsuarioId(), inscricao.getCampanhaId()));
    }

    @Test
    void deveEncontrarInscricaoPorCampanha() {
        UsuarioCampanha inscricao = criarCenarioInscricao();

        inscricaoRepository.inscrever(inscricao);

        List<UsuarioCampanha> inscricoes = inscricaoRepository.findAllByCampanha(inscricao.getCampanhaId());
        assertFalse(inscricoes.isEmpty(), "O usuário deveria estar inscrito nessa campanha.");

        UsuarioCampanha inscricaoEncontrada = inscricoes.getFirst();

        assertEquals(inscricao.getCampanhaId(), inscricaoEncontrada.getCampanhaId());
        assertEquals(inscricao.getUsuarioId(), inscricaoEncontrada.getUsuarioId());
        assertEquals(inscricao.getDataInscricao(), inscricaoEncontrada.getDataInscricao());
    }

    @Test
    void deveContarInscricoesPorUsuario() {
        UsuarioCampanha inscricao = criarCenarioInscricao();
        inscricaoRepository.inscrever(inscricao);

        assertEquals(1, inscricaoRepository.contarInscricoesByUsuarioId(inscricao.getUsuarioId()));
    }

    @Test
    void deveContarInscricoesPorCampanha() {
        UsuarioCampanha inscricao = criarCenarioInscricao();
        inscricaoRepository.inscrever(inscricao);

        assertEquals(1, inscricaoRepository.contarInscricoesByCampanhaId(inscricao.getCampanhaId()));
    }

    @Test
    void deveRemoverInscricao() {
        UsuarioCampanha inscricao = criarCenarioInscricao();
        inscricaoRepository.inscrever(inscricao);

        inscricaoRepository.removerInscricao(inscricao.getUsuarioId(), inscricao.getCampanhaId());

        List<UsuarioCampanha> inscricoesCampanha = inscricaoRepository.findAllByCampanha(inscricao.getCampanhaId());
        assertTrue(inscricoesCampanha.isEmpty());
        List<UsuarioCampanha> inscricoesUsuario = inscricaoRepository.findAllByUsuario(inscricao.getUsuarioId());
        assertTrue(inscricoesUsuario.isEmpty());
    }

    @Test
    void deveRemoverInscricoesEmCampanha() {
        UsuarioCampanha inscricao = criarCenarioInscricao();
        inscricaoRepository.inscrever(inscricao);

        inscricaoRepository.removerHistoricoDeInscricoesByCampanha(inscricao.getCampanhaId());

        List<UsuarioCampanha> inscricoesCampanha = inscricaoRepository.findAllByCampanha(inscricao.getCampanhaId());
        assertTrue(inscricoesCampanha.isEmpty());
        List<UsuarioCampanha> inscricoesUsuario = inscricaoRepository.findAllByUsuario(inscricao.getUsuarioId());
        assertTrue(inscricoesUsuario.isEmpty());
    }

    private UsuarioCampanha criarCenarioInscricao() {
        Campanha campanha = salvarCampanha();
        Voluntario voluntario = criarVoluntarioPadrao();

        UsuarioCampanha inscricao = new UsuarioCampanha();
        inscricao.setCampanhaId(campanha.getCampanhaId());
        inscricao.setUsuarioId(voluntario.getUsuarioId());
        inscricao.setDataInscricao(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        return inscricao;
    }

    private Campanha salvarCampanha() {
        Long localId = criarLocalPadrao().getLocalId();
        Long usuarioId = criarUsuarioPadrao().getUsuarioId();
        criarPatrocinadorPadrao(usuarioId);

        Campanha campanha = criarCampanhaPadrao(
                localId,
                usuarioId
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

    private Voluntario criarVoluntarioPadrao() {
        Long usuarioId = criarUsuarioPadrao().getUsuarioId();
        Voluntario voluntario = new Voluntario();
        voluntario.setUsuarioId(usuarioId);
        voluntario.setNome("Teste");
        voluntario.setCpf("00000000000");
        voluntario.setDataNascimento(LocalDate.now());

        voluntarioRepository.insertVoluntario(
                usuarioId,
                voluntario.getNome(),
                voluntario.getCpf(),
                voluntario.getDataNascimento()
        );

        return voluntario;
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

    private Campanha criarCampanhaPadrao(Long localId, Long patrocinadorId) {
        Campanha campanha = new Campanha();
        campanha.setPatrocinadorId(patrocinadorId);
        campanha.setLocalId(localId);
        campanha.setMetaFundos(BigDecimal.TEN);
        campanha.setNome("Campanha Teste");
        campanha.setDescricao("Descricao Teste");
        campanha.setDataInicio(LocalDateTime.now().minusDays(30).truncatedTo(ChronoUnit.SECONDS));
        campanha.setDataFim(LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS));


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
