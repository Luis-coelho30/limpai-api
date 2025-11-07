package br.com.limpai.projeto_limpai.controller.entity;

import br.com.limpai.projeto_limpai.dto.*;
import br.com.limpai.projeto_limpai.exception.user.CredenciaisIncorretasException;
import br.com.limpai.projeto_limpai.model.entity.Patrocinador;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.model.entity.Voluntario;
import br.com.limpai.projeto_limpai.service.entity.PatrocinadorService;
import br.com.limpai.projeto_limpai.service.entity.VoluntarioService;
import br.com.limpai.projeto_limpai.service.security.CookieService;
import br.com.limpai.projeto_limpai.service.security.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final PatrocinadorService patrocinadorService;
    private final VoluntarioService voluntarioService;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, CookieService cookieService,
                          JwtService jwtService, PatrocinadorService patrocinadorService, VoluntarioService voluntarioService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
        this.patrocinadorService = patrocinadorService;
        this.voluntarioService = voluntarioService;
    }

    @PostMapping("/cadastrar/voluntario")
    public ResponseEntity<LoginResponseDTO> cadastrarVoluntario(@RequestBody VoluntarioCadastroDTO voluntarioDTO) {
        Voluntario voluntario;

        voluntario = voluntarioService.cadastrarVoluntario(
                            voluntarioDTO.nome(),
                            voluntarioDTO.cpf(),
                            voluntarioDTO.dataNascimento(),
                            voluntarioDTO.email(),
                            voluntarioDTO.senha(),
                            voluntarioDTO.telefone()
                    );

        UserDetails userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(voluntarioDTO.email());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        cookieService.setRefreshToken(refreshToken);

        String role = userDetails.getAuthorities().stream()
                .findFirst().map(GrantedAuthority::getAuthority).orElseThrow();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new LoginResponseDTO(accessToken, new UsuarioDTO(voluntario.getVoluntarioId().toString(), userDetails.getUsername(), role))
        );
    }

    @PostMapping("/cadastrar/patrocinador")
    public ResponseEntity<LoginResponseDTO> cadastrarPatrocinador(@RequestBody PatrocinadorCadastroDTO patrocinadorDTO) {
        Patrocinador patrocinador;

        patrocinador = patrocinadorService.cadastrarPatrocinador(
                patrocinadorDTO.razaoSocial(),
                patrocinadorDTO.nomeFantasia(),
                patrocinadorDTO.cnpj(),
                patrocinadorDTO.email(),
                patrocinadorDTO.senha(),
                patrocinadorDTO.telefone()
        );

        UserDetails userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(patrocinadorDTO.email());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        cookieService.setRefreshToken(refreshToken);

        String role = userDetails.getAuthorities().stream()
                .findFirst().map(GrantedAuthority::getAuthority).orElseThrow();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new LoginResponseDTO(accessToken,
                        new UsuarioDTO(patrocinador.getPatrocinadorId().toString(),
                        userDetails.getUsername(),
                        role)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> verificarUsuario(@RequestBody LoginDTO loginDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.email(),
                            loginDTO.senha()
                    )
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            cookieService.setRefreshToken(refreshToken);

            String role = userDetails.getAuthorities().stream()
                    .findFirst().map(GrantedAuthority::getAuthority).orElseThrow();

            return ResponseEntity.status(HttpStatus.OK).body(
                    new LoginResponseDTO(accessToken,
                            new UsuarioDTO(userDetails.getId().toString(),
                                    userDetails.getUsername(),
                                    role)
                    )
            );

        } catch (AuthenticationException ex) {
            throw new CredenciaisIncorretasException("Email ou senha incorretos.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        cookieService.clearRefreshTokenCookie();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> renovarToken() {

        try {

            String refreshToken = cookieService.getRefreshTokenCookie()
                    .orElseThrow(() -> new JwtException("Refresh token não encontrado")); // 401

            String email = jwtService.extractUserMail(refreshToken);

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

            if (!jwtService.validateToken(refreshToken, userDetails)) {
                throw new JwtException("Refresh token inválido"); // 401
            }

            String novoAccessToken = jwtService.generateToken(userDetails);

            String role = userDetails.getAuthorities().stream()
                    .findFirst().map(GrantedAuthority::getAuthority).orElseThrow();

            return ResponseEntity.ok(new LoginResponseDTO(novoAccessToken,
                    new UsuarioDTO(userDetails.getId().toString(),
                            userDetails.getUsername(),
                            role)
                    )
            );

        } catch (JwtException | UsernameNotFoundException e) {
            cookieService.clearRefreshTokenCookie();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
