package br.com.limpai.projeto_limpai.controller.join;

import br.com.limpai.projeto_limpai.dto.request.entity.DoacaoDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.inscricao.MinhaInscricaoDTO;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.service.join.InscricaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/campanha")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @GetMapping("/inscricoes/me")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'PATROCINADOR')")
    public ResponseEntity<Page<MinhaInscricaoDTO>> listarMinhasCampanhas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PageableDefault(size = 10, sort = "data_inscricao") Pageable pageable) {
        Page<MinhaInscricaoDTO> inscricoes = inscricaoService.getAllByUsuario(userDetails.getId(), pageable);

        return ResponseEntity.ok(inscricoes);
    }


    @PostMapping("/{id}/inscricao")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'PATROCINADOR')")
    public ResponseEntity<Void> inscrever(@PathVariable("id") Long campanhaId,
                                          @RequestBody(required = false) DoacaoDTO doacaoDTO,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BigDecimal doacao = (doacaoDTO != null) ? doacaoDTO.valor() : BigDecimal.ZERO;

        inscricaoService.inscrever(userDetails.getId(), campanhaId, doacao);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/inscricao")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelarInscricao(@PathVariable("id") Long campanhaId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        inscricaoService.desinscrever(userDetails.getId(), campanhaId);
        return ResponseEntity.noContent().build();
    }
}
