package tareas.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.*;
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public usuarios guardarUsuario(usuarios usuario){

        if(usuarioRepository.findByDocumento(usuario.getDocumento()).isPresent()){
            throw new RuntimeException("El usuario ya existe");
        }

        String passwordCifrada = passwordEncoder.encode(usuario.getContrasenna());

        usuario.setContrasenna(passwordCifrada);

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String documento){
        usuarioRepository.deleteByDocumento(documento);
    }

    public List<usuarios> obtenerTop10Estudiantes() {
        // PageRequest.of(numeroDePagina, tamannoDePagina)
        // La página 0 es la primera página. El 10 es la cantidad de filas que quieres.
        Pageable topDiez = PageRequest.of(0, 10);

        return usuarioRepository.findTop10ByOrderByPuntosDesc(topDiez);
    }

    public List<usuarios> obtenerSiguientes10Estudiantes() {
        // Si en la app móvil el usuario desliza hacia abajo (scroll), pides la página 1
        Pageable siguientesDiez = PageRequest.of(1, 10);

        return usuarioRepository.findTop10ByOrderByPuntosDesc(siguientesDiez);
    }
}
