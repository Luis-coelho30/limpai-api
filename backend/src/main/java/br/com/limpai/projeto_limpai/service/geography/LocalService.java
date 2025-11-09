package br.com.limpai.projeto_limpai.service.geography;

import br.com.limpai.projeto_limpai.dto.request.cadastro.LocalRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;
import br.com.limpai.projeto_limpai.exception.geography.CidadeNaoEncontradaException;
import br.com.limpai.projeto_limpai.exception.geography.LocalJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.repository.geography.CidadeRepository;
import br.com.limpai.projeto_limpai.repository.geography.LocalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;
    private final CidadeRepository cidadeRepository;

    public LocalService(LocalRepository localRepository, CidadeRepository cidadeRepository) {
        this.localRepository = localRepository;
        this.cidadeRepository = cidadeRepository;
    }

    @Transactional(readOnly = true)
    public Page<LocalResponseDTO> listarLocais(Long cidadeIdFilter, Pageable pageable) {
        List<LocalResponseDTO> views;
        long total;

        if (cidadeIdFilter != null) {
            views = localRepository.findLocalByCidade(cidadeIdFilter, pageable);
            total = localRepository.countByCidadeId(cidadeIdFilter);
        } else {
            views = localRepository.findAllLocal(pageable);
            total = localRepository.count();
        }

        return new PageImpl<>(views, pageable, total);
    }

    @Transactional(readOnly = true)
    public LocalResponseDTO buscarPorId(Long localId) {
        return localRepository.findLocalById(localId)
                .orElseThrow(() -> new LocalNaoEncontradoException(localId));
    }

    @Transactional(readOnly = true)
    public boolean verificarLocalById(Long localId) {
        return localRepository.existsById(localId);
    }

    @Transactional
    public LocalResponseDTO criarLocal(LocalRequestDTO dto) {

        validarCidade(dto.cidadeId());
        if (localRepository.existsByCepAndEndereco(dto.cep(), dto.endereco())) {
            throw new LocalJaCadastradoException(dto.endereco(), dto.cep());
        }

        Local local = new Local();
        local.setNome(dto.nome());
        local.setEndereco(dto.endereco());
        local.setCep(dto.cep());
        local.setCidadeId(dto.cidadeId());

        local = localRepository.save(local);

        return buscarPorId(local.getLocalId());
    }

    @Transactional
    public LocalResponseDTO atualizarLocal(Long localId, LocalRequestDTO dto) {
        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new LocalNaoEncontradoException(localId));

        if (!local.getCidadeId().equals(dto.cidadeId())) {
            validarCidade(dto.cidadeId());
        }

        boolean enderecoMudou = !local.getCep().equals(dto.cep()) || !local.getEndereco().equals(dto.endereco());
        if (enderecoMudou && localRepository.existsByCepAndEndereco(dto.cep(), dto.endereco())) {
            throw new LocalJaCadastradoException(dto.endereco(), dto.cep());
        }

        local.setNome(dto.nome());
        local.setEndereco(dto.endereco());
        local.setCep(dto.cep());
        local.setCidadeId(dto.cidadeId());

        localRepository.save(local);

        return buscarPorId(local.getLocalId());
    }

    @Transactional
    public void apagarLocal(Long localId) {
        if (!localRepository.existsById(localId)) {
            throw new LocalNaoEncontradoException(localId);
        }

        localRepository.deleteById(localId);
    }

    private void validarCidade(Long cidadeId) {
        if (!cidadeRepository.existsById(cidadeId)) {
            throw new CidadeNaoEncontradaException(cidadeId);
        }
    }
}
