package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.voluntario.AtualizarVoluntarioRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.voluntario.VoluntarioDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.voluntario.VoluntarioMinDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.VoluntarioCadastroDTO;
import br.com.limpai.projeto_limpai.exception.user.CpfJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.repository.entity.VoluntarioRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final UsuarioService usuarioService;

    public VoluntarioService(VoluntarioRepository voluntarioRepository, UsuarioService usuarioService) {
        this.voluntarioRepository = voluntarioRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<VoluntarioMinDTO> listarVoluntarios() {
        Iterable<Voluntario> iterable = voluntarioRepository.findAll();
        List<VoluntarioMinDTO> lista = new ArrayList<>();
        iterable.forEach(voluntario -> lista.add(VoluntarioMinDTO.from(voluntario)));

        return lista;
    }

    @Transactional(readOnly = true)
    public VoluntarioMinDTO getVoluntarioPublicoById(Long voluntarioId) {
        return VoluntarioMinDTO.from(voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(voluntarioId)));
    }

    @Transactional(readOnly = true)
    public VoluntarioDTO getVoluntarioPrivadoById(Long voluntarioId) {
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(voluntarioId));

        Usuario usuario = usuarioService.getUsuarioPorId(voluntarioId);

        return VoluntarioDTO.from(voluntario, usuario);
    }

    @Transactional
    public RegistroDTO cadastrarVoluntario(VoluntarioCadastroDTO voluntarioDTO) {
        if(voluntarioRepository.existsByCpf(voluntarioDTO.cpf())) {
            throw new CpfJaCadastradoException(voluntarioDTO.cpf());
        }

        Usuario usuarioCriado = usuarioService.criarUsuarioBase(
                voluntarioDTO.email(),
                voluntarioDTO.senha(),
                voluntarioDTO.telefone(),
                UsuarioEnum.VOLUNTARIO
        );

        voluntarioRepository.insertVoluntario(
                usuarioCriado.getUsuarioId(),
                voluntarioDTO.nome(),
                voluntarioDTO.cpf(),
                voluntarioDTO.dataNascimento()
        );

        return new RegistroDTO(
                usuarioCriado.getUsuarioId(),
                voluntarioDTO.nome(),
                usuarioCriado
        );
    }

    @Transactional
    public RegistroDTO atualizarVoluntario(Long voluntarioId, VoluntarioCadastroDTO voluntarioDTO) {
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(voluntarioId));

        if (verificarCpfJaExiste(voluntario.getCpf(), voluntarioDTO.cpf())) {
            throw new CpfJaCadastradoException(voluntarioDTO.cpf());
        }

        Usuario usuarioAtualizado;

        usuarioAtualizado = usuarioService.
                atualizarUsuario(
                        voluntarioId,
                        voluntarioDTO.email(),
                        voluntarioDTO.senha(),
                        voluntarioDTO.telefone(),
                        UsuarioEnum.VOLUNTARIO
                );

        voluntario.setNome(voluntarioDTO.nome());
        voluntario.setCpf(voluntarioDTO.cpf());
        voluntario.setDataNascimento(voluntarioDTO.dataNascimento());

        voluntarioRepository.save(voluntario);

        return new RegistroDTO(
                voluntario.getVoluntarioId(),
                voluntario.getNome(),
                usuarioAtualizado
        );
    }

    @Transactional
    public VoluntarioDTO atualizarParcial(Long id, AtualizarVoluntarioRequestDTO voluntarioRequestDTO) {
        Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (voluntarioRequestDTO.nome() != null && !voluntarioRequestDTO.nome().isBlank()) {
            voluntario.setNome(voluntarioRequestDTO.nome());
        }

        if (voluntarioRequestDTO.dataNascimento() != null) {
            voluntario.setDataNascimento(voluntarioRequestDTO.dataNascimento());
        }

        if (voluntarioRequestDTO.cpf() != null && !voluntarioRequestDTO.cpf().isBlank()) {
            if (verificarCpfJaExiste(voluntario.getCpf(), voluntarioRequestDTO.cpf())) {
                throw new CpfJaCadastradoException(voluntarioRequestDTO.cpf());
            }

            voluntario.setNome(voluntarioRequestDTO.cpf());
        }

        Usuario usuario;

        if (voluntarioRequestDTO.telefone() != null && !voluntarioRequestDTO.telefone().isBlank()) {
            usuario = usuarioService.atualizarTelefone(voluntario.getVoluntarioId(), voluntarioRequestDTO.telefone());
        } else {
            usuario = usuarioService.getUsuarioPorId(id);
        }

        voluntarioRepository.save(voluntario);

        return VoluntarioDTO.from(voluntario, usuario);
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

    private boolean verificarCpfJaExiste(String cpf, String cpfNovo) {
        return cpf.equals(cpfNovo) && voluntarioRepository.existsByCpf(cpfNovo);
    }

}
