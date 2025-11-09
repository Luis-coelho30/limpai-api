package br.com.limpai.projeto_limpai.controller.geography;

import br.com.limpai.projeto_limpai.dto.request.cadastro.LocalRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.local.LocalResponseDTO;
import br.com.limpai.projeto_limpai.service.geography.LocalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/local")
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @GetMapping
    public ResponseEntity<Page<LocalResponseDTO>> listarLocais(@PageableDefault(sort = "nome") Pageable pageable,
                                                               @RequestParam(required = false) Long cidadeId) {
        return ResponseEntity.ok(localService.listarLocais(cidadeId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATROCINADOR')")
    public ResponseEntity<LocalResponseDTO> criarLocal(@RequestBody @Valid LocalRequestDTO localDTO) {
        LocalResponseDTO novoLocal = localService.criarLocal(localDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLocal);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocalResponseDTO> atualizarLocal(@PathVariable Long id, @RequestBody @Valid LocalRequestDTO localDTO) {
        LocalResponseDTO novoLocal = localService.atualizarLocal(id, localDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoLocal);
    }

}
