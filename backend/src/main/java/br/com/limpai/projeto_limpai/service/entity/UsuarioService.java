package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.EmailJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.UsuarioRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario criarUsuarioBase(String email, String senha, String telefone, UsuarioEnum tipoUsuario) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new EmailJaCadastradoException(email);
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(tipoUsuario);

        return usuarioRepository.save(usuario);
    }


    public Usuario atualizarUsuario(Long usuarioId, String email, String senha, String telefone, UsuarioEnum tipoUsuario) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(tipoUsuario);

        return usuarioRepository.save(usuario);
    }

    public void apagarUsuario(Long usuarioId, String email) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        usuarioRepository.delete(usuario);
    }

    public Usuario carregarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Email: " + email));
    }
}
