package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador.PatrocinadorDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador.PatrocinadorMinDTO;
import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.PatrocinadorCadastroDTO;
import br.com.limpai.projeto_limpai.exception.user.CnpjJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.PatrocinadorRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatrocinadorService {

    private final PatrocinadorRepository patrocinadorRepository;
    private final UsuarioService usuarioService;

    public PatrocinadorService(PatrocinadorRepository patrocinadorRepository, UsuarioService usuarioService) {
        this.patrocinadorRepository = patrocinadorRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<PatrocinadorMinDTO> listarPatrocinadores() {
        Iterable<Patrocinador> iterable = patrocinadorRepository.findAll();
        List<PatrocinadorMinDTO> lista = new ArrayList<>();
        iterable.forEach(patrocinador -> lista.add(PatrocinadorMinDTO.from(patrocinador)));

        return lista;
    }

    @Transactional(readOnly = true)
    public PatrocinadorDTO getPatrocinadorById(Long patrocinadorId) {
        Patrocinador patrocinador = patrocinadorRepository.findById(patrocinadorId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(patrocinadorId));

        Usuario usuario = usuarioService.getUsuarioPorId(patrocinadorId);

        return PatrocinadorDTO.from(patrocinador, usuario);
    }

    @Transactional
    public RegistroDTO cadastrarPatrocinador(PatrocinadorCadastroDTO patrocinadorDTO) {
        if(patrocinadorRepository.existsByCnpj(patrocinadorDTO.cnpj())) {
            throw new CnpjJaCadastradoException(patrocinadorDTO.cnpj());
        }

        Usuario usuarioCriado = usuarioService.criarUsuarioBase(
                patrocinadorDTO.email(),
                patrocinadorDTO.senha(),
                patrocinadorDTO.telefone(),
                UsuarioEnum.PATROCINADOR
        );

        patrocinadorRepository.insertPatrocinador(
                usuarioCriado.getUsuarioId(),
                patrocinadorDTO.razaoSocial(),
                patrocinadorDTO.nomeFantasia(),
                patrocinadorDTO.cnpj()
        );

        return new RegistroDTO(
                usuarioCriado.getUsuarioId(),
                patrocinadorDTO.nomeFantasia(),
                usuarioCriado
        );
    }

    @Transactional
    public RegistroDTO atualizarPatrocinador(Long patrocinadorId, PatrocinadorCadastroDTO patrocinadorDTO) {
        Patrocinador patrocinador = patrocinadorRepository.findById(patrocinadorId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(patrocinadorId));

        if (!patrocinador.getCnpj().equals(patrocinadorDTO.cnpj()) && patrocinadorRepository.existsByCnpj(patrocinadorDTO.cnpj())) {
            throw new CnpjJaCadastradoException(patrocinadorDTO.cnpj());
        }

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(
                                            patrocinadorId,
                                            patrocinadorDTO.email(),
                                            patrocinadorDTO.senha(),
                                            patrocinadorDTO.telefone(),
                                            UsuarioEnum.PATROCINADOR
                                    );

        patrocinador.setRazaoSocial(patrocinadorDTO.razaoSocial());
        patrocinador.setNomeFantasia(patrocinadorDTO.nomeFantasia());
        patrocinador.setCnpj(patrocinadorDTO.cnpj());

        patrocinadorRepository.save(patrocinador);

        return new RegistroDTO(
                usuarioAtualizado.getUsuarioId(),
                patrocinadorDTO.nomeFantasia(),
                usuarioAtualizado
        );
    }

    @Transactional
    public void apagarPatrocinador(Long patrocinadorId) {
        Optional<Patrocinador> patrocinadorOpt = patrocinadorRepository.findById(patrocinadorId);

        if(patrocinadorOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException(patrocinadorId);
        }

        patrocinadorRepository.delete(patrocinadorOpt.get());
        usuarioService.apagarUsuario(patrocinadorId);
    }
}
