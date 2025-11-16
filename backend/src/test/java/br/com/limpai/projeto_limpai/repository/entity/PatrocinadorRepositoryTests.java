package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.AbstractIntegrationTest;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatrocinadorRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    private final String NOME_FANTASIA = "Limpai LTDA";
    private final String RAZAO_SOCIAL = "Limpai Empresa";
    private final String CNPJ = "11111111111111";

    @BeforeEach
    void setup() {
        patrocinadorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveSalvarNovoPatrocinador() {
        Usuario usuario = criarUsuarioPadrao();

        Patrocinador patrocinador = criarPatrocinadorPadrao();

        patrocinadorRepository.
                insertPatrocinador(
                        usuario.getUsuarioId(),
                        patrocinador.getRazaoSocial(),
                        patrocinador.getNomeFantasia(),
                        patrocinador.getCnpj()
                );

        Optional<Patrocinador> patrocinadorOpt = patrocinadorRepository.findById(usuario.getUsuarioId());
        assertTrue(patrocinadorOpt.isPresent(), "O patrocinador deveria ter sido encontrado no banco");
        Patrocinador patrocinadorEncontrado = patrocinadorOpt.get();

        assertEquals(NOME_FANTASIA, patrocinadorEncontrado.getNomeFantasia());
        assertEquals(RAZAO_SOCIAL, patrocinadorEncontrado.getRazaoSocial());
        assertEquals(CNPJ, patrocinadorEncontrado.getCnpj());
    }

    @Test
    void deveEncontrarPatrocinadorPorCnpj() {
        Usuario usuario = criarUsuarioPadrao();

        Patrocinador patrocinador = criarPatrocinadorPadrao();

        patrocinadorRepository.
                insertPatrocinador(
                        usuario.getUsuarioId(),
                        patrocinador.getRazaoSocial(),
                        patrocinador.getNomeFantasia(),
                        patrocinador.getCnpj()
                );

        assertTrue(patrocinadorRepository.existsByCnpj(patrocinador.getCnpj()));
    }

    @Test
    void deveDeletarPatrocinador() {
        Usuario usuario = criarUsuarioPadrao();

        Patrocinador patrocinador = criarPatrocinadorPadrao();

        patrocinadorRepository.
                insertPatrocinador(
                        usuario.getUsuarioId(),
                        patrocinador.getRazaoSocial(),
                        patrocinador.getNomeFantasia(),
                        patrocinador.getCnpj()
                );

        Optional<Patrocinador> patrocinadorOpt = patrocinadorRepository.findById(usuario.getUsuarioId());
        assertTrue(patrocinadorOpt.isPresent(), "O patrocinador deveria ter sido encontrado no banco");
        Patrocinador patrocinadorEncontrado = patrocinadorOpt.get();

        patrocinadorRepository.delete(patrocinadorEncontrado);
    }

    @Test
    void deveDeletarPatrocinadorPorId() {
        Usuario usuario = criarUsuarioPadrao();

        Patrocinador patrocinador = criarPatrocinadorPadrao();

        patrocinadorRepository.
                insertPatrocinador(
                        usuario.getUsuarioId(),
                        patrocinador.getRazaoSocial(),
                        patrocinador.getNomeFantasia(),
                        patrocinador.getCnpj()
                );

        Optional<Patrocinador> patrocinadorOpt = patrocinadorRepository.findById(usuario.getUsuarioId());
        assertTrue(patrocinadorOpt.isPresent(), "O patrocinador deveria ter sido encontrado no banco");
        Patrocinador patrocinadorEncontrado = patrocinadorOpt.get();

        patrocinadorRepository.deleteById(patrocinadorEncontrado.getUsuarioId());
    }

    private Usuario criarUsuarioPadrao() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@limpai.com");
        usuario.setSenha("123");
        usuario.setTelefone("11 92151-1511");
        usuario.setTipo(UsuarioEnum.VOLUNTARIO);

        return usuarioRepository.save(usuario);
    }

    private Patrocinador criarPatrocinadorPadrao() {
        Patrocinador patrocinador = new Patrocinador();
        patrocinador.setNomeFantasia(NOME_FANTASIA);
        patrocinador.setRazaoSocial(RAZAO_SOCIAL);
        patrocinador.setCnpj(CNPJ);

        return patrocinador;
    }
}
