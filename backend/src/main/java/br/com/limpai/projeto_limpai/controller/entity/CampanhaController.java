package br.com.limpai.projeto_limpai.controller.entity;

import br.com.limpai.projeto_limpai.dto.request.entity.CriarCampanhaDTO;
import br.com.limpai.projeto_limpai.dto.request.entity.DoacaoDTO;
import br.com.limpai.projeto_limpai.dto.request.entity.EstenderPrazoDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.campanha.CampanhaDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.campanha.CampanhaMinDTO;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.service.entity.CampanhaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campanha")
public class CampanhaController {

    private final CampanhaService campanhaService;

    public CampanhaController(CampanhaService campanhaService) {
        this.campanhaService = campanhaService;
    }

    @GetMapping
    public ResponseEntity<Page<CampanhaMinDTO>> listarTodas(@PageableDefault(size = 12, sort = "dataFim") Pageable pageable,
                                                            @RequestParam(required = false, defaultValue = "false") boolean historico,
                                                            @RequestParam(required = false) Long cidadeId,
                                                            @RequestParam(required = false) Long estadoId) {
        Page<CampanhaMinDTO> campanhas;

        /* Realiza o mapeamento para o método correto, a depender do filtro aplicado */
        if (cidadeId != null) {
            campanhas = historico ?
                    campanhaService.listarExpiradasPorCidade(cidadeId, pageable) :
                    campanhaService.listarAtivasPorCidade(cidadeId, pageable);
        } else if (estadoId != null) {
            campanhas = historico ?
                    campanhaService.listarExpiradasPorEstado(estadoId, pageable) :
                    campanhaService.listarAtivasPorEstado(estadoId, pageable);
        } else {
            campanhas = historico ?
                    campanhaService.listarHistoricoCampanhas(pageable) :
                    campanhaService.listarCampanhasAtivas(pageable);
        }

        return ResponseEntity.ok(campanhas);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATROCINADOR')")
    public ResponseEntity<Page<CampanhaMinDTO>> listarMinhasCampanhas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @PageableDefault(size = 10, sort = "dataInicio") Pageable pageable) {

        return ResponseEntity.ok(
                campanhaService.listarMinhasCampanhas(userDetails.getId(), pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaDTO> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(campanhaService.getCampanhaById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATROCINADOR')")
    public ResponseEntity<CampanhaDTO> criarCampanha(@RequestBody CriarCampanhaDTO dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CampanhaDTO novaCampanha = campanhaService.criarCampanha(userDetails.getId(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaCampanha);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATROCINADOR') and @securityService.isDonoDaCampanha(#id, principal.id))")
    public ResponseEntity<CampanhaDTO> atualizarCampanha(@PathVariable Long id, @RequestBody CriarCampanhaDTO dto) {

        return ResponseEntity.ok(campanhaService.atualizarCampanha(id, dto));
    }

    @PatchMapping("/{id}/encerrar")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATROCINADOR') and @securityService.isDonoDaCampanha(#id, principal.id))")
    public ResponseEntity<CampanhaDTO> encerrarCampanha(@PathVariable Long id) {

        return ResponseEntity.ok(campanhaService.encerrarCampanha(id));
    }

    @PatchMapping("/{id}/estender")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATROCINADOR') and @securityService.isDonoDaCampanha(#id, principal.id))")
    public ResponseEntity<CampanhaDTO> estenderPrazo(@PathVariable Long id, @RequestBody EstenderPrazoDTO prazoDTO) {

        return ResponseEntity.ok(campanhaService.estenderPrazo(id, prazoDTO.novaDataFim()));
    }

    @PatchMapping("/{id}/estender")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATROCINADOR')")
    public ResponseEntity<Void> registrarDoacao(@PathVariable Long id, @RequestBody DoacaoDTO doacaoDTO) {
        campanhaService.registrarDoacao(id, doacaoDTO.valor());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarCampanha(@PathVariable Long id) {
        campanhaService.apagarCampanha(id);

        return ResponseEntity.noContent().build();
    }
}
