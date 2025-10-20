package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.campanha.CampanhaNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Campanha;
import br.com.limpai.projeto_limpai.repository.entity.CampanhaRepository;
import br.com.limpai.projeto_limpai.service.geography.LocalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final LocalService localService;

    public CampanhaService(CampanhaRepository campanhaRepository, LocalService localService) {
        this.campanhaRepository = campanhaRepository;
        this.localService = localService;
    }

    public Campanha getCampanhaById(Long campanhaId) {
        return campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));
    }

    public List<Campanha> listarCampanhas() {
        Iterable<Campanha> iterable = campanhaRepository.findAll();
        List<Campanha> lista = new ArrayList<>();
        iterable.forEach(lista::add);

        return lista;
    }

    public Campanha criarCampanha(String nome, String descricao, BigDecimal metaFundos, LocalDateTime dataInicio, LocalDateTime dataFim, Long localId) {
        if(!localService.verificarLocalById(localId)) {
            throw new LocalNaoEncontradoException(localId);
        }

        Campanha campanhaSalva = new Campanha();
        campanhaSalva.setNome(nome);
        campanhaSalva.setDescricao(descricao);
        campanhaSalva.setMetaFundos(metaFundos);
        campanhaSalva.setDataInicio(dataInicio);
        campanhaSalva.setDataFim(dataFim);
        campanhaSalva.setLocalId(localId);

        return campanhaRepository.save(campanhaSalva);
    }

    public Campanha atualizarCampanha(Long campanhaId, String nome, String descricao, BigDecimal metaFundos, LocalDateTime dataInicio, LocalDateTime dataFim, Long localId) {
        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        if(!Objects.equals(campanha.getLocalId(), localId)) {
            if (!localService.verificarLocalById(localId)) {
                throw new LocalNaoEncontradoException(localId);
            }
        }

        campanha.setNome(nome);
        campanha.setDescricao(descricao);
        campanha.setMetaFundos(metaFundos);
        campanha.setDataInicio(dataInicio);
        campanha.setDataFim(dataFim);
        campanha.setLocalId(localId);

        return campanhaRepository.save(campanha);
    }

    public void apagarCampanha(Long campanhaId) {
        Campanha local = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontradaException(campanhaId));

        campanhaRepository.delete(local);
    }
}
