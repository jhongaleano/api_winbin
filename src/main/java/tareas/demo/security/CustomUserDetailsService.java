package tareas.demo.security;

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

        if (u.getContrasenna() == null || u.getContrasenna().isBlank()) {
            throw new UsernameNotFoundException("Usuario sin contraseña configurada");
        }

        // En BD: ESTUDIANTE, ADMIN — Spring usa ROLE_* para hasRole(...)
        String rol = u.getRol() != null ? u.getRol().trim().toUpperCase() : "Estudiante";
        String authority = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;

        return User.builder()
                .username(String.valueOf(u.getDocumento()))
                .password(u.getContrasenna())
                .authorities(authority)
                .build();
    }
}
