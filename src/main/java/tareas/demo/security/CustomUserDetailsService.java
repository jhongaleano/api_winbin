package tareas.demo.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException { 
        usuarios u = usuarioRepository.findByDocumento(documento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (Boolean.FALSE.equals(u.getActivo())) {
            throw new DisabledException("La cuenta del usuario ha sido desactivada.");
        }

        if (u.getContrasenna() == null || u.getContrasenna().isBlank()) {
            throw new UsernameNotFoundException("Usuario sin contraseña configurada");
        }

        String rolLimpio = (u.getRol() != null && !u.getRol().isBlank()) ? u.getRol().trim().toUpperCase() : "USER";
        String authority = rolLimpio.startsWith("ROLE_") ? rolLimpio : "ROLE_" + rolLimpio;

        return User.builder()
                .username(u.getDocumento())
                .password(u.getContrasenna())
                .disabled(!Boolean.TRUE.equals(u.getActivo()))
                .roles(authority)
                .build();   
    }
}
