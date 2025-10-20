package br.com.limpai.projeto_limpai.service.join;

import br.com.limpai.projeto_limpai.exception.campanha.CampanhaExpiradaException;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioJaEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioNaoEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.join.UsuarioCampanha;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.service.entity.CampanhaService;
import br.com.limpai.projeto_limpai.service.entity.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscricaoService {

    private final UsuarioCampanhaRepository inscricaoRepository;
    private final CampanhaService campanhaService;
    private final UsuarioService usuarioService;

    public InscricaoService(UsuarioCampanhaRepository inscricaoRepository, CampanhaService campanhaService, UsuarioService usuarioService) {
        this.inscricaoRepository = inscricaoRepository;
        this.campanhaService = campanhaService;
        this.usuarioService = usuarioService;
    }

    public List<UsuarioCampanha> getAllByUsuario(Long usuarioId) {
        return inscricaoRepository.findAllByUsuario(usuarioId);
    }

    public List<UsuarioCampanha> getAllByCampanha(Long campanhaId) {
        return inscricaoRepository.findAllByCampanha(campanhaId);
    }

    public void inscrever(Long usuarioId, Long campanhaId) {
        if(!usuarioService.verificarUsuarioPorId(usuarioId)) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        }

        if(!campanhaService.verificarCampanhaPorId(campanhaId)) {
            throw new CampanhaNaoEncontradaException(campanhaId);
        }

        if(campanhaService.verificarCampanhaExpirada(campanhaId)) {
            throw new CampanhaExpiradaException(campanhaId);
        }

        if(inscricaoRepository.existsByUsuarioAndCampanha(usuarioId, campanhaId)) {
            throw new UsuarioJaEstaInscritoException(usuarioId, campanhaId);
        }

        UsuarioCampanha inscricao = new UsuarioCampanha();
        inscricao.setUsuarioId(usuarioId);
        inscricao.setCampanhaId(campanhaId);
        inscricao.setDataInscricao(LocalDateTime.now());

        inscricaoRepository.inscrever(inscricao);
    }

    public void desinscrever(Long usuarioId, Long campanhaId) {
        if(!usuarioService.verificarUsuarioPorId(usuarioId)) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        }

        if(!campanhaService.verificarCampanhaPorId(campanhaId)) {
            throw new CampanhaNaoEncontradaException(campanhaId);
        }

        if(campanhaService.verificarCampanhaExpirada(campanhaId)) {
            throw new CampanhaExpiradaException(campanhaId);
        }

        if(!inscricaoRepository.existsByUsuarioAndCampanha(usuarioId, campanhaId)) {
            throw new UsuarioNaoEstaInscritoException(usuarioId, campanhaId);
        }

        inscricaoRepository.removerInscricao(usuarioId, campanhaId);
    }
}
