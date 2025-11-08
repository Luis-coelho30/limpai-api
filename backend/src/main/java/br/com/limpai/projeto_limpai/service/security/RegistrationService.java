package br.com.limpai.projeto_limpai.service.security;

import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.PatrocinadorCadastroDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.VoluntarioCadastroDTO;
import br.com.limpai.projeto_limpai.service.entity.PatrocinadorService;
import br.com.limpai.projeto_limpai.service.entity.VoluntarioService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final VoluntarioService voluntarioService;
    private final PatrocinadorService patrocinadorService;

    public RegistrationService(VoluntarioService voluntarioService, PatrocinadorService patrocinadorService) {
        this.voluntarioService = voluntarioService;
        this.patrocinadorService = patrocinadorService;
    }

    public RegistroDTO cadastrarVoluntario(VoluntarioCadastroDTO voluntarioDTO) {
        return voluntarioService.cadastrarVoluntario(voluntarioDTO);
    }

    public RegistroDTO cadastrarPatrocinador(PatrocinadorCadastroDTO patrocinadorDTO) {
        return patrocinadorService.cadastrarPatrocinador(patrocinadorDTO);
    }
}
