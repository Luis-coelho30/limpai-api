package br.com.limpai.projeto_limpai.repository.entity;

import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.repository.AbstractIntegrationTest;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VoluntarioRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final String NOME = "Luis";
    private final String CPF = "111.111.111-11";
    private final LocalDate DATA_NASCIMENTO = LocalDate.now();

    @BeforeEach
    public void setup() {
        voluntarioRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveSalvarNovoVoluntario() {
        Usuario u = criarUsuarioBase();

        Voluntario voluntario = criarVoluntarioBase();

        voluntarioRepository
                .insertVoluntario(
                        u.getUsuarioId(),
                        voluntario.getNome(),
                        voluntario.getCpf(),
                        voluntario.getDataNascimento()
                );

        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findById(u.getUsuarioId());
        assertTrue(voluntarioOpt.isPresent(), "O voluntário deveria ter sido encontrado no banco");
        Voluntario voluntarioEncontrado = voluntarioOpt.get();

        assertEquals(NOME, voluntarioEncontrado.getNome());
        assertEquals(CPF, voluntarioEncontrado.getCpf());
        assertEquals(DATA_NASCIMENTO, voluntarioEncontrado.getDataNascimento());
    }

    @Test
    void deveEncontrarVoluntarioPorCpf() {
        Usuario usuario = criarUsuarioBase();

        Voluntario voluntario = criarVoluntarioBase();

        voluntarioRepository.
                insertVoluntario(
                        usuario.getUsuarioId(),
                        voluntario.getNome(),
                        voluntario.getCpf(),
                        voluntario.getDataNascimento()
                );

        assertTrue(voluntarioRepository.existsByCpf(voluntario.getCpf()));
    }

    @Test
    void deveDeletarVoluntario() {
        Usuario usuario = criarUsuarioBase();

        Voluntario voluntario = criarVoluntarioBase();

        voluntarioRepository.
                insertVoluntario(
                        usuario.getUsuarioId(),
                        voluntario.getNome(),
                        voluntario.getCpf(),
                        voluntario.getDataNascimento()
                );

        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findById(usuario.getUsuarioId());
        assertTrue(voluntarioOpt.isPresent(), "O voluntário deveria ter sido encontrado no banco");
        Voluntario voluntarioEncontrado = voluntarioOpt.get();

        voluntarioRepository.delete(voluntarioEncontrado);
    }

    @Test
    void deveDeletarVoluntarioPorId() {
        Usuario usuario = criarUsuarioBase();

        Voluntario voluntario = criarVoluntarioBase();

        voluntarioRepository.
                insertVoluntario(
                        usuario.getUsuarioId(),
                        voluntario.getNome(),
                        voluntario.getCpf(),
                        voluntario.getDataNascimento()
                );

        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findById(usuario.getUsuarioId());
        assertTrue(voluntarioOpt.isPresent(), "O voluntário deveria ter sido encontrado no banco");
        Voluntario voluntarioEncontrado = voluntarioOpt.get();

        voluntarioRepository.deleteById(voluntarioEncontrado.getUsuarioId());
    }

    private Usuario criarUsuarioBase() {
        Usuario u = new Usuario();
        u.setEmail("voluntario@teste.com");
        u.setSenha("123");
        u.setTelefone("11 99999-9999");
        u.setTipo(UsuarioEnum.VOLUNTARIO);

        return usuarioRepository.save(u);
    }

    private Voluntario criarVoluntarioBase() {
        Voluntario voluntario = new Voluntario();
        voluntario.setNome(NOME);
        voluntario.setCpf(CPF);
        voluntario.setDataNascimento(DATA_NASCIMENTO);
        return voluntario;
    }

}
