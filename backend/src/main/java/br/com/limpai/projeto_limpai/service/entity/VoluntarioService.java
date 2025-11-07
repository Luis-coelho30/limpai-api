package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.CpfJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.repository.entity.VoluntarioRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class    VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final UsuarioService usuarioService;

    public VoluntarioService(VoluntarioRepository voluntarioRepository, UsuarioService usuarioService) {
        this.voluntarioRepository = voluntarioRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<Voluntario> listarVoluntarios() {
        Iterable<Voluntario> iterable = voluntarioRepository.findAll();
        List<Voluntario> lista = new ArrayList<>();
        iterable.forEach(lista::add);

        return lista;
    }

    @Transactional(readOnly = true)
    public Voluntario getVoluntarioById(Long voluntarioId) {
        return voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(voluntarioId));
    }

    @Transactional
    public Voluntario cadastrarVoluntario(String nome, String cpf, LocalDate dataNascimento, String email, String senha, String telefone) {
        if(voluntarioRepository.existsByCpf(cpf)) {
            throw new CpfJaCadastradoException(cpf);
        }

        Usuario usuarioCriado = usuarioService.criarUsuarioBase(email, senha, telefone, UsuarioEnum.VOLUNTARIO);

        voluntarioRepository.insertVoluntario(usuarioCriado.getUsuarioId(), nome, cpf, dataNascimento);

        return new Voluntario(
                usuarioCriado.getUsuarioId(),
                nome,
                cpf,
                dataNascimento
        );
    }

    @Transactional
    public Voluntario atualizarVoluntario(Long voluntarioId, String nome, String cpf, LocalDate dataNascimento, String email, String senha, String telefone) {
        Voluntario voluntario = getVoluntarioById(voluntarioId);

        if (!voluntario.getCpf().equals(cpf) && voluntarioRepository.existsByCpf(cpf)) {
            throw new CpfJaCadastradoException(cpf);
        }

        usuarioService.atualizarUsuario(
                voluntarioId,
                email,
                senha,
                telefone,
                UsuarioEnum.VOLUNTARIO
        );

        voluntario.setNome(nome);
        voluntario.setCpf(cpf);
        voluntario.setDataNascimento(dataNascimento);

        return voluntarioRepository.save(voluntario);
    }

    @Transactional
    public void apagarVoluntario(Long voluntarioId) {
        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findById(voluntarioId);

        if(voluntarioOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException(voluntarioId);
        }

        voluntarioRepository.delete(voluntarioOpt.get());
        usuarioService.apagarUsuario(voluntarioId);
    }
}
