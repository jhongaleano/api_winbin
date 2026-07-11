package tareas.demo.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;

import java.util.List;
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public usuarios guardarUsuario(usuarios usuario){

        if(usuarioRepository.findByDocumento(usuario.getDocumento()).isPresent()){
            throw new RuntimeException("El usuario con documento " + usuario.getDocumento() + " ya existe");
        }

        String passwordCifrada = passwordEncoder.encode(usuario.getContrasenna());
        usuario.setContrasenna(passwordCifrada);

        usuario.setPuntos(0);
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty() || usuario.getRol().contains("ADMIN")) {
            usuario.setRol("ROLE_USER"); 
        }
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String documento){
        if (!usuarioRepository.existsById(documento)) {
            throw new RuntimeException("El usuario no existe");
        }
        usuarioRepository.deleteById(documento);
    }

    public usuarios obtenerPerfil(String documento) {
    return usuarioRepository.findByDocumento(documento)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
}

    public List<usuarios> obtenerTop10Estudiantes() {
        Pageable topDiez = PageRequest.of(0, 10);

        return usuarioRepository.findTop10ByOrderByPuntosDesc(topDiez);
    }

    public List<usuarios> obtenerSiguientes10Estudiantes() {
        Pageable siguientesDiez = PageRequest.of(1, 10);
        return usuarioRepository.findTop10ByOrderByPuntosDesc(siguientesDiez);
    }

    public usuarios cambiarRolUsuario(String documento, String nuevoRol) {
        usuarios usuario = usuarioRepository.findById(documento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rolFormateado = nuevoRol.startsWith("ROLE_") ? nuevoRol : "ROLE_" + nuevoRol;
        usuario.setRol(rolFormateado);

        return usuarioRepository.save(usuario);
    }

    public usuarios reactivarUsuario(String documento) {
        usuarios usuario = usuarioRepository.findByDocumentoIncluyendoInactivos(documento)
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario registrado en el sistema."));

        if (Boolean.TRUE.equals(usuario.getActivo())) {
            throw new RuntimeException("El usuario ya se encuentra activo.");
        }

        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public List<usuarios> listarUsuariosInactivos() {
        return usuarioRepository.findAllInactivos();
    }

    public List<usuarios> listarTodosIncluyendoInactivos() {
        return usuarioRepository.findAllIncluyendoInactivos();
    }
}
