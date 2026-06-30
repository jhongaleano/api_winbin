package tareas.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public usuarios guardarUsuario(usuarios usuario){
        String passwordCifrada = passwordEncoder.encode(usuario.getContrasenna());

        usuario.setContrasenna(passwordCifrada);

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String documento){
        usuarioRepository.deleteByDocumento(documento);
    }
}
