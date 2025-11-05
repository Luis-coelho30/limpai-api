package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.UserDetailsImpl;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioService usuarioService;

    public UserDetailsServiceImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioService.carregarUsuarioPorEmail(email);
            return new UserDetailsImpl(usuario);
        } catch (UsuarioNaoEncontradoException e) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }
    }
}
