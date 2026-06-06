package tareas.demo.controller;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tareas.demo.services.UsuarioService;


@RestController
@RequestMapping("/api/usuarios")
public class usuarioController {
    @Autowired
    private UsuarioRepository repositorio;

    @GetMapping
    public List<usuarios> listar(){
        return repositorio.findAll();
    }
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody usuarios nuevoUsuario){
        try {
            usuarios usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
            return ResponseEntity.ok(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }
    }
    
}
