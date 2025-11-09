package br.com.limpai.projeto_limpai.service.security;

import br.com.limpai.projeto_limpai.dto.internal.RegistroDTO;
import br.com.limpai.projeto_limpai.dto.request.auth.LoginRequestDTO;
import br.com.limpai.projeto_limpai.dto.request.auth.AlterarEmailRequestDTO;
import br.com.limpai.projeto_limpai.dto.request.auth.AlterarSenhaRequestDTO;
import br.com.limpai.projeto_limpai.dto.response.auth.LoginResponseDTO;
import br.com.limpai.projeto_limpai.dto.response.auth.UsuarioViewDTO;
import br.com.limpai.projeto_limpai.exception.security.JwtParsingException;
import br.com.limpai.projeto_limpai.exception.user.CredenciaisIncorretasException;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.service.entity.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UsuarioService usuarioService;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService,
                       CookieService cookieService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.usuarioService = usuarioService;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
            );
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            return gerarSessao(userDetails.getUsuario(), null);

        } catch (AuthenticationException e) {
            throw new CredenciaisIncorretasException("Email ou senha incorretos.");
        }
    }

    public LoginResponseDTO refresh() {
        String refreshToken = cookieService.getRefreshTokenCookie()
                .orElseThrow(() -> new JwtParsingException("Refresh token não encontrado"));

        String email = jwtService.extractUserMail(refreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        if (!jwtService.validateToken(refreshToken, userDetails)) {
            throw new JwtParsingException("Refresh token inválido");
        }

        return gerarSessao(userDetails.getUsuario(), null);
    }

    public void logout() {
        cookieService.clearRefreshTokenCookie();
    }

    public LoginResponseDTO alterarSenha(AlterarSenhaRequestDTO dto, UserDetailsImpl userDetails) {
        validarSenhaAtual(userDetails.getUsername(), dto.senhaAtual());

        Usuario usuario = usuarioService.atualizarSenha(
                userDetails.getUsuario().getUsuarioId(),
                dto.senhaNova()
        );

        return gerarSessao(usuario, null);
    }

    public LoginResponseDTO alterarEmail(AlterarEmailRequestDTO dto, UserDetailsImpl userDetails) {
        validarSenhaAtual(userDetails.getUsername(), dto.senhaAtual());

        Usuario usuario = usuarioService.atualizarEmail(
                userDetails.getUsuario().getUsuarioId(),
                dto.novoEmail()
        );

        return gerarSessao(usuario, null);
    }

    public LoginResponseDTO gerarSessaoPosCadastro(RegistroDTO registro) {
        return gerarSessao(registro.usuario(), registro.nome());
    }

    private void validarSenhaAtual(String email, String senhaAtual) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senhaAtual));
        } catch (AuthenticationException e) {
            throw new CredenciaisIncorretasException("Senha atual incorreta.");
        }
    }

    private LoginResponseDTO gerarSessao(Usuario usuario, String nomePerfil) {
        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        cookieService.setRefreshToken(refreshToken);

        UsuarioViewDTO usuarioView = new UsuarioViewDTO(
                usuario.getUsuarioId().toString(),
                nomePerfil,
                usuario.getEmail(),
                usuario.getTipo().name()
        );

        return new LoginResponseDTO(accessToken, usuarioView);
    }
}
