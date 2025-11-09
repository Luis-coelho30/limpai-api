package br.com.limpai.projeto_limpai.controller.entity;

import br.com.limpai.projeto_limpai.dto.request.entity.AtualizarPatrocinadorRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador.PatrocinadorDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.patrocinador.PatrocinadorMinDTO;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.service.entity.PatrocinadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("patrocinador")
public class PatrocinadorController {

    private final PatrocinadorService patrocinadorService;

    public PatrocinadorController(PatrocinadorService patrocinadorService) {
        this.patrocinadorService = patrocinadorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PATROCINADOR', 'ADMIN')")
    public ResponseEntity<List<PatrocinadorMinDTO>> listAllVoluntarios() {
        List<PatrocinadorMinDTO> patrocinadores = patrocinadorService.listarPatrocinadores();
        return ResponseEntity.ok(patrocinadores);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATROCINADOR', 'ADMIN')")
    public ResponseEntity<PatrocinadorDTO> getVoluntarioById(@PathVariable Long id) {
        PatrocinadorDTO patrocinador = patrocinadorService.getPatrocinadorById(id);
        return ResponseEntity.ok(patrocinador);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('PATROCINADOR', 'ADMIN')")
    public ResponseEntity<PatrocinadorDTO> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        PatrocinadorDTO patrocinador = patrocinadorService.getPatrocinadorById(userDetails.getId());
        return ResponseEntity.ok(patrocinador);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('PATROCINADOR', 'ADMIN')")
    public ResponseEntity<PatrocinadorDTO> updateMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AtualizarPatrocinadorRequestDTO patrocinadorRequestDTO
    ) {
        PatrocinadorDTO patrocinadorAtualizado = patrocinadorService.atualizarParcial(userDetails.getId(), patrocinadorRequestDTO);
        return ResponseEntity.ok(patrocinadorAtualizado);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAnyRole('PATROCINADOR', 'ADMIN')")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        patrocinadorService.apagarPatrocinador(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
