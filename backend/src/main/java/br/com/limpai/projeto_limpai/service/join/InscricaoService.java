package br.com.limpai.projeto_limpai.service.join;

import br.com.limpai.projeto_limpai.dto.response.perfil.inscricao.MinhaInscricaoDTO;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaExpiradaException;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioJaEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.campanha.UsuarioNaoEstaInscritoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.join.UsuarioCampanha;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.service.entity.CampanhaService;
import br.com.limpai.projeto_limpai.service.entity.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Transactional(readOnly = true)
    public Page<MinhaInscricaoDTO> getAllByUsuario(Long usuarioId, Pageable pageable) {
        return campanhaService.listarMinhasInscricoes(usuarioId, pageable);
    }

    @Transactional(readOnly = true)
    public List<UsuarioCampanha> getAllByCampanha(Long campanhaId) {
        return inscricaoRepository.findAllByCampanha(campanhaId);
    }

    @Transactional
    public void inscrever(Long usuarioId, Long campanhaId, BigDecimal valorDoacao) {
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

        if(Objects.equals(valorDoacao, BigDecimal.ZERO)) {
            campanhaService.registrarDoacao(campanhaId, valorDoacao);
        }

        UsuarioCampanha inscricao = new UsuarioCampanha();
        inscricao.setUsuarioId(usuarioId);
        inscricao.setCampanhaId(campanhaId);
        inscricao.setDataInscricao(LocalDateTime.now());

        inscricaoRepository.inscrever(inscricao);
    }

    @Transactional
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
