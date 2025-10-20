package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.CnpjJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.PatrocinadorRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.stereotype.Service;

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

    public List<Patrocinador> listarPatrocinadores() {
        Iterable<Patrocinador> iterable = patrocinadorRepository.findAll();
        List<Patrocinador> lista = new ArrayList<>();
        iterable.forEach(lista::add);

        return lista;
    }

    public Patrocinador getPatrocinadorById(Long patrocinadorId) {
        return patrocinadorRepository.findById(patrocinadorId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(patrocinadorId));
    }

    public Patrocinador cadastrarPatrocinador(String razaoSocial, String nomeFantasia, String cnpj, String email, String senha, String telefone) {
        if(patrocinadorRepository.existsByCnpj(cnpj)) {
            throw new CnpjJaCadastradoException(cnpj);
        }

        Usuario usuarioCriado = usuarioService.criarUsuarioBase(email, senha, telefone, UsuarioEnum.PATROCINADOR);

        patrocinadorRepository.insertPatrocinador(usuarioCriado.getUsuarioId(), razaoSocial,nomeFantasia, cnpj);

        return new Patrocinador(
                usuarioCriado.getUsuarioId(),
                razaoSocial,
                nomeFantasia,
                cnpj
        );
    }

    public Patrocinador atualizarPatrocinador(Long patrocinadorId, String razaoSocial, String nomeFantasia, String cnpj, String email, String senha, String telefone) {
        Patrocinador patrocinador = getPatrocinadorById(patrocinadorId);

        if (!patrocinador.getCnpj().equals(cnpj) && patrocinadorRepository.existsByCnpj(cnpj)) {
            throw new CnpjJaCadastradoException(cnpj);
        }

        usuarioService.atualizarUsuario(
                patrocinadorId,
                email,
                senha,
                telefone,
                UsuarioEnum.PATROCINADOR
        );

        patrocinador.setRazaoSocial(razaoSocial);
        patrocinador.setNomeFantasia(nomeFantasia);
        patrocinador.setCnpj(cnpj);

        return patrocinadorRepository.save(patrocinador);
    }

    public void apagarPatrocinador(Long patrocinadorId) {
        Optional<Patrocinador> patrocinadorOpt = patrocinadorRepository.findById(patrocinadorId);

        if(patrocinadorOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException(patrocinadorId);
        }

        patrocinadorRepository.delete(patrocinadorOpt.get());
        usuarioService.apagarUsuario(1L);
    }
}
