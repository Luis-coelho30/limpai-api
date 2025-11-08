package br.com.limpai.projeto_limpai.controller.entity;

import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.auth.LoginRequestDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.PatrocinadorCadastroDTO;
import br.com.limpai.projeto_limpai.dto.request.cadastro.VoluntarioCadastroDTO;
import br.com.limpai.projeto_limpai.dto.request.auth.AlterarEmailRequestDTO;
import br.com.limpai.projeto_limpai.dto.request.auth.AlterarSenhaRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.auth.LoginResponseDTO;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.service.security.AuthService;
import br.com.limpai.projeto_limpai.service.security.RegistrationService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RegistrationService registrationService;

    public AuthController(AuthService authService, RegistrationService registrationService) {
        this.authService = authService;
        this.registrationService = registrationService;
    }

    @PostMapping("/cadastrar/voluntario")
    public ResponseEntity<LoginResponseDTO> cadastrarVoluntario(@RequestBody VoluntarioCadastroDTO voluntarioDTO) {
        RegistroDTO registro = registrationService.cadastrarVoluntario(voluntarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.gerarSessaoPosCadastro(registro));
    }

    @PostMapping("/cadastrar/patrocinador")
    public ResponseEntity<LoginResponseDTO> cadastrarPatrocinador(@RequestBody PatrocinadorCadastroDTO patrocinadorDTO) {
        RegistroDTO registro = registrationService.cadastrarPatrocinador(patrocinadorDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.gerarSessaoPosCadastro(registro));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh() {
        try {
            return ResponseEntity.ok(authService.refresh());
        } catch (JwtException | UsernameNotFoundException e) {
            authService.logout();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<LoginResponseDTO> alterarSenha(@RequestBody AlterarSenhaRequestDTO dto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(authService.alterarSenha(dto, userDetails));
    }

    @PutMapping("/alterar-email")
    public ResponseEntity<LoginResponseDTO> alterarEmail(@RequestBody AlterarEmailRequestDTO dto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(authService.alterarEmail(dto, userDetails));
    }
}
