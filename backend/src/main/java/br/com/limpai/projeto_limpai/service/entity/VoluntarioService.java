package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.VoluntarioDTO;
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
    public List<VoluntarioDTO> listarVoluntarios() {
        Iterable<Voluntario> iterable = voluntarioRepository.findAll();
        List<VoluntarioDTO> lista = new ArrayList<>();
        iterable.forEach(voluntario -> lista.add(VoluntarioDTO.from(voluntario)));

        return lista;
    }

    @Transactional(readOnly = true)
    public VoluntarioDTO getVoluntarioById(Long voluntarioId) {
        return VoluntarioDTO.from(voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(voluntarioId)));
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

        if (!voluntario.getCpf().equals(voluntarioDTO.cpf()) && voluntarioRepository.existsByCpf(voluntarioDTO.cpf())) {
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
    public void apagarVoluntario(Long voluntarioId) {
        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findById(voluntarioId);

        if(voluntarioOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException(voluntarioId);
        }

        voluntarioRepository.delete(voluntarioOpt.get());
        usuarioService.apagarUsuario(voluntarioId);
    }
}
