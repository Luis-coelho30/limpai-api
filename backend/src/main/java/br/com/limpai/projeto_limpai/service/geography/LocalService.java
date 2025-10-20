package br.com.limpai.projeto_limpai.service.geography;

import br.com.limpai.projeto_limpai.exception.geography.LocalJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.geography.LocalNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.geography.Local;
import br.com.limpai.projeto_limpai.repository.geography.LocalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public List<Local> listarLocais() {
        Iterable<Local> iterable = localRepository.findAll();
        List<Local> lista = new ArrayList<>();
        iterable.forEach(lista::add);

        return lista;
    }

    public Local getLocalById(Long localId) {
        return localRepository.findById(localId)
                .orElseThrow(() -> new LocalNaoEncontradoException(localId));
    }

    public Local criarLocal(String nome, String endereco, String cep, Long cidadeId) {
        if(localRepository.existsByCepAndEndereco(cep, endereco)) {
            throw new LocalJaCadastradoException(endereco, cep);
        }

        Local local = new Local();
        local.setNome(nome);
        local.setEndereco(endereco);
        local.setCep(cep);
        local.setCidadeId(cidadeId);

        return localRepository.save(local);
    }

    public Local atualizarLocal(Long localId, String nome, String endereco, String cep, Long cidadeId) {
        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new LocalNaoEncontradoException(1L));

        if(verificarNovoCepOuEndereco(cep, endereco, local.getCep(), local.getEndereco())) {
            if(localRepository.existsByCepAndEndereco(cep, endereco)) {
                throw new LocalJaCadastradoException(endereco, cep);
            }
        }

        local.setNome(nome);
        local.setEndereco(endereco);
        local.setCep(cep);
        local.setCidadeId(cidadeId);

        return localRepository.save(local);
    }

    public void apagarLocal(Long localId) {
        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new LocalNaoEncontradoException(localId));

        localRepository.delete(local);
    }

    private boolean verificarNovoCepOuEndereco(String cepNovo, String enderecoNovo, String cepAntigo, String enderecoAntigo) {
        return cepNovo.equals(cepAntigo) || enderecoNovo.equals(enderecoAntigo);
    }
}
