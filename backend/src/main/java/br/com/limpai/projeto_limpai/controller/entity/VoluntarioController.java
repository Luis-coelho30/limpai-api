package br.com.limpai.projeto_limpai.controller.entity;

import br.com.limpai.projeto_limpai.dto.request.voluntario.AtualizarVoluntarioRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.voluntario.VoluntarioDTO;
import br.com.limpai.projeto_limpai.dto.response.perfil.voluntario.VoluntarioMinDTO;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.service.entity.VoluntarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voluntario")
public class VoluntarioController {
    private final VoluntarioService voluntarioService;

    public VoluntarioController(VoluntarioService voluntarioService) {
        this.voluntarioService = voluntarioService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'ADMIN')")
    public ResponseEntity<List<VoluntarioMinDTO>> listAllVoluntarios() {
        List<VoluntarioMinDTO> voluntarios = voluntarioService.listarVoluntarios();
        return ResponseEntity.ok(voluntarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'ADMIN')")
    public ResponseEntity<VoluntarioMinDTO> getVoluntarioById(@PathVariable Long id) {
        VoluntarioMinDTO voluntario = voluntarioService.getVoluntarioPublicoById(id);
        return ResponseEntity.ok(voluntario);
    }

    @GetMapping("/personal/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VoluntarioDTO> getVoluntarioPrivado(@PathVariable Long id) {
        VoluntarioDTO voluntario = voluntarioService.getVoluntarioPrivadoById(id);
        return ResponseEntity.ok(voluntario);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'ADMIN')")
    public ResponseEntity<VoluntarioDTO> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        VoluntarioDTO voluntario = voluntarioService.getVoluntarioPrivadoById(userDetails.getId());
        return ResponseEntity.ok(voluntario);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'ADMIN')")
    public ResponseEntity<VoluntarioDTO> updateMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AtualizarVoluntarioRequestDTO voluntarioRequestDTO
    ) {
        VoluntarioDTO voluntarioAtualizado = voluntarioService.atualizarParcial(userDetails.getId(), voluntarioRequestDTO);
        return ResponseEntity.ok(voluntarioAtualizado);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'ADMIN')")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        voluntarioService.apagarVoluntario(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
