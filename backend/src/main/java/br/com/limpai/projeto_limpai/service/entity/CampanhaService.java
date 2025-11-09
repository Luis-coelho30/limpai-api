package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.dto.request.entity.CriarCampanhaDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.CampanhaViewDTO;
import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import br.com.limpai.projeto_limpai.repository.entity.CampanhaRepository;
import br.com.limpai.projeto_limpai.repository.join.UsuarioCampanhaRepository;
import br.com.limpai.projeto_limpai.service.geography.LocalService;
import br.com.limpai.projeto_limpai.service.join.InscricaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Campanha getCampanhaById(Long campanhaId) {
        return campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));
    }

    @Transactional(readOnly = true)
    public List<Campanha> listarCampanhas() {
        return campanhaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CampanhaViewDTO> listarCampanhasAtivas(Pageable pageable) {
        return campanhaRepository.findCampanhasNaoExpiradas(pageable);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaViewDTO> listarHistoricoCampanhas(Pageable pageable) {
        return campanhaRepository.findCampanhasExpiradas(pageable);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaViewDTO> listarAtivasPorCidade(Long cidadeId, Pageable pageable) {
        return campanhaRepository.findAtivasByCidade(cidadeId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaViewDTO> listarExpiradasPorCidade(Long cidadeId, Pageable pageable) {
        return campanhaRepository.findExpiradasByCidade(cidadeId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaViewDTO> listarAtivasPorEstado(Long estadoId, Pageable pageable) {
        return campanhaRepository.findAtivasByEstado(estadoId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CampanhaViewDTO> listarExpiradasPorEstado(Long estadoId, Pageable pageable) {
        return campanhaRepository.findExpiradasByEstado(estadoId, pageable);
    }

    @Transactional
    public Campanha criarCampanha(Long donoId, CriarCampanhaDTO campanhaDTO) {
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

        return campanhaRepository.save(campanhaSalva);
    }

    @Transactional
    public Campanha atualizarCampanha(Long campanhaId, CriarCampanhaDTO campanhaDTO) {
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

        return campanhaRepository.save(campanha);
    }

    @Transactional
    public void apagarCampanha(Long campanhaId) {
        Campanha local = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        inscricaoRepository.removerHistoricoDeInscricoesByCampanha(campanhaId);
        campanhaRepository.delete(local);
    }
}
