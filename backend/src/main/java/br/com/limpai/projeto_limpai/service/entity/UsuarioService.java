package br.com.limpai.projeto_limpai.service.entity;

import br.com.limpai.projeto_limpai.exception.user.EmailJaCadastradoException;
import br.com.limpai.projeto_limpai.exception.user.UsuarioNaoEncontradoException;
import br.com.limpai.projeto_limpai.model.entity.Usuario;
import br.com.limpai.projeto_limpai.repository.entity.UsuarioRepository;
import br.com.limpai.projeto_limpai.types.UsuarioEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public boolean verificarUsuarioPorId(Long usuarioId) {
        return usuarioRepository.existsById(usuarioId);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }

    @Transactional
    public Usuario criarUsuarioBase(String email, String senha, String telefone, UsuarioEnum tipoUsuario) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new EmailJaCadastradoException(email);
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(encoder.encode(senha));
        usuario.setTelefone(telefone);
        usuario.setTipoUsuario(tipoUsuario);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizarUsuario(Long usuarioId, String email, String senha, String telefone, UsuarioEnum tipoUsuario) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        boolean emailMudou = !usuario.getEmail().equals(email);
        if (emailMudou && usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(email);
        }

        if (senha != null && !senha.isEmpty() && !encoder.matches(senha, usuario.getSenha())) {
            usuario.setSenha(encoder.encode(senha));
        }

        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setTipoUsuario(tipoUsuario);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void apagarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario carregarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));
    }
}
