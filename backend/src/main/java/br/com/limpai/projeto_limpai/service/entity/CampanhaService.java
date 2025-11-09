package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.request.entity.CriarCampanhaDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.campanha.CampanhaDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.campanha.CampanhaMinDTO;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaExpiradaException;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.campanha.DataInvalidaException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import br.com.limpai.projeto_limpai.repository.entity.CampanhaRepository;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.service.geography.LocalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final UsuarioCampanhaRepository inscricaoRepository;
    private final LocalService localService;

    public CampanhaService(CampanhaRepository campanhaRepository, UsuarioCampanhaRepository inscricaoRepository, LocalService localService) {
        this.campanhaRepository = campanhaRepository;
        this.inscricaoRepository = inscricaoRepository;
        this.localService = localService;
    }

    @Transactional(readOnly = true)
    public boolean verificarCampanhaPorId(Long campanhaId) {
        return campanhaRepository.existsById(campanhaId);
    }

    @Transactional(readOnly = true)
    public boolean verificarCampanhaExpirada(Long campanhaId) {
        return campanhaRepository.isExpired(campanhaId);
    }

    @Transactional(readOnly = true)
    public CampanhaDTO getCampanhaById(Long campanhaId) {
        return CampanhaDTO.from(campanhaRepository.findCampanhaById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId)));
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarMinhasCampanhas(Long patrocinadorId, Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findByPatrocinadorId(patrocinadorId, pageable);
        long total = campanhaRepository.countByPatrocinadorId(patrocinadorId);

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarCampanhasAtivas(Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findCampanhasNaoExpiradas(pageable);
        long total = campanhaRepository.countCampanhasNaoExpiradas();

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarHistoricoCampanhas(Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findCampanhasExpiradas(pageable);
        long total = campanhaRepository.countCampanhasExpiradas();

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarAtivasPorCidade(Long cidadeId, Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findAtivasByCidade(cidadeId, pageable);
        long total = campanhaRepository.countAtivasByCidade(cidadeId);

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarExpiradasPorCidade(Long cidadeId, Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findExpiradasByCidade(cidadeId, pageable);
        long total = campanhaRepository.countExpiradasByCidade(cidadeId);

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarAtivasPorEstado(Long estadoId, Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findAtivasByEstado(estadoId, pageable);
        long total = campanhaRepository.countAtivasByEstado(estadoId);

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaMinDTO> listarExpiradasPorEstado(Long estadoId, Pageable pageable) {
        List<CampanhaMinDTO> campanhas = campanhaRepository.findExpiradasByEstado(estadoId, pageable);
        long total = campanhaRepository.countExpiradasByEstado(estadoId);

        return new PageImpl<>(campanhas, pageable, total);
    }

    @Transactional
    public void registrarDoacao(Long campanhaId, BigDecimal valor) {
        campanhaRepository.adicionarFundos(campanhaId, valor);
    }

    @Transactional
    public CampanhaDTO criarCampanha(Long donoId, CriarCampanhaDTO campanhaDTO) {
        if(!localService.verificarLocalById(campanhaDTO.localId())) {
            throw new LocalNaoEncontradoException(campanhaDTO.localId());
        }

        Campanha campanhaSalva = new Campanha();
        campanhaSalva.setNome(campanhaDTO.nome());
        campanhaSalva.setDescricao(campanhaDTO.descricao());
        campanhaSalva.setMetaFundos(campanhaDTO.metaFundos());
        campanhaSalva.setDataInicio(campanhaDTO.dataInicio());
        campanhaSalva.setDataFim(campanhaDTO.dataFim());
        campanhaSalva.setPatrocinadorId(donoId);
        campanhaSalva.setLocalId(campanhaDTO.localId());

        Campanha novaCampanha = campanhaRepository.save(campanhaSalva);

        return CampanhaDTO.from(campanhaRepository.findCampanhaById(novaCampanha.getCampanhaId())
                .orElseThrow(() -> new CampanhaNaoEncontradaException(novaCampanha.getCampanhaId())));
    }

    @Transactional
    public CampanhaDTO encerrarCampanha(Long campanhaId) {
        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        if (campanha.getDataFim().isBefore(LocalDateTime.now())) {
            throw new CampanhaExpiradaException(campanhaId);
        }

        campanha.setDataFim(LocalDateTime.now());
        campanhaRepository.save(campanha);

        return getCampanhaById(campanhaId);
    }

    @Transactional
    public CampanhaDTO estenderPrazo(Long campanhaId, LocalDateTime novaDataFim) {
        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        if (novaDataFim.isBefore(LocalDateTime.now()) || novaDataFim.isBefore(campanha.getDataFim())) {
            throw new DataInvalidaException("A nova data deve ser posterior à data de encerramento atual.");
        }

        campanha.setDataFim(novaDataFim);
        campanhaRepository.save(campanha);

        return getCampanhaById(campanhaId);
    }

    @Transactional
    public CampanhaDTO atualizarCampanha(Long campanhaId, CriarCampanhaDTO campanhaDTO) {
        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        if(!Objects.equals(campanha.getLocalId(), campanhaDTO.localId())) {
            if (!localService.verificarLocalById(campanhaDTO.localId())) {
                throw new LocalNaoEncontradoException(campanhaDTO.localId());
            }
        }

        campanha.setNome(campanhaDTO.nome());
        campanha.setDescricao(campanhaDTO.descricao());
        campanha.setMetaFundos(campanhaDTO.metaFundos());
        campanha.setDataInicio(campanhaDTO.dataInicio());
        campanha.setDataFim(campanhaDTO.dataFim());
        campanha.setLocalId(campanhaDTO.localId());

        Campanha campanhaAtualizada = campanhaRepository.save(campanha);

        return CampanhaDTO.from(campanhaRepository.findCampanhaById(campanhaAtualizada.getCampanhaId())
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaAtualizada.getCampanhaId())));
    }

    @Transactional
    public void apagarCampanha(Long campanhaId) {
        Campanha local = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        inscricaoRepository.removerHistoricoDeInscricoesByCampanha(campanhaId);
        campanhaRepository.delete(local);
    }
}
