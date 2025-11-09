package br.com.limpai.projeto_limpai.service.security;

import br.com.limpai.projeto_limpai.repository.entity.CampanhaRepository;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final CampanhaRepository campanhaRepository;

    public SecurityService(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }

    public boolean isDonoDaCampanha(Long campanhaId, Long patrocinadorId) {
        return campanhaRepository.isPatrocinadorDono(campanhaId, patrocinadorId);
    }
}
