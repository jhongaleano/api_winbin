package tareas.demo.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tareas.demo.dto.CambiarPasswordDTO;
import tareas.demo.dto.UsuarioRegistroDTO;
import tareas.demo.models.Cursos;
import tareas.demo.models.usuarios;
import tareas.demo.repository.CursoRepository;
import tareas.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;


import java.util.List;
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CursoRepository cursoRepository;
    

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CursoRepository cursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cursoRepository = cursoRepository; 
    }

    public usuarios guardarUsuario(UsuarioRegistroDTO registroDTO) {

        if(usuarioRepository.findByDocumento(registroDTO.getDocumento()).isPresent()){
            throw new RuntimeException("El usuario con documento " + registroDTO.getDocumento() + " ya existe");
        }

        Cursos cursoEncontrado = cursoRepository.findById(registroDTO.getId_curso())
                .orElseThrow(() -> new RuntimeException("El curso con ID " + registroDTO.getId_curso() + " no existe"));

        usuarios nuevoUsuario = new usuarios();
        nuevoUsuario.setDocumento(registroDTO.getDocumento());
        nuevoUsuario.setNombre(registroDTO.getNombre());
        String passwordCifrada = passwordEncoder.encode(registroDTO.getContrasenna());
        nuevoUsuario.setContrasenna(passwordCifrada);
        nuevoUsuario.setCurso(cursoEncontrado);
        nuevoUsuario.setPuntos(0);
        nuevoUsuario.setRol(usuarios.Rol.ESTUDIANTE);
        nuevoUsuario.setActivo(true);
        return usuarioRepository.save(nuevoUsuario);
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


    public usuarios obtenerUsuarioTOP() {
        Pageable topUno = PageRequest.of(0, 1);
        List<usuarios> resultado = usuarioRepository.findTop10ByOrderByPuntosDesc(topUno);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public List<usuarios> obtenerSiguientes10Estudiantes() {
        Pageable siguientesDiez = PageRequest.of(1, 10);
        return usuarioRepository.findTop10ByOrderByPuntosDesc(siguientesDiez);
    }

    public usuarios cambiarRolUsuario(String documento, String nuevoRol) {
        usuarios usuario = usuarioRepository.findById(documento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            String rolLimpio = nuevoRol.replace("ROLE_", "").toUpperCase();
            usuarios.Rol rolEnum = usuarios.Rol.valueOf(rolLimpio);
            usuario.setRol(rolEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("El rol proporcionado no es válido. Opciones: ESTUDIANTE, ADMIN");
        }
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


    @Transactional
    public void actualizarAvatar(String documento, String nuevaUrl) {
        usuarios usuario = usuarioRepository.findByDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setAvatarUrl(nuevaUrl);
        usuarioRepository.save(usuario); // Todo se guardará y la auditoría se disparará en el COMMIT único
    }

    public void cambiarPasswordUsuarioAutenticado(CambiarPasswordDTO dto) {

        String documentoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        usuarios usuario = usuarioRepository.findById(documentoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getContrasenna())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setContrasenna(passwordEncoder.encode(dto.getNuevaPassword()));

        usuarioRepository.save(usuario);
    }
}
