package tareas.demo.controller;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

     @PutMapping("/{documento}")
    public usuarios actualizar(@PathVariable String documento, @RequestBody usuarios usuarioActualizado) {
        return repositorio.findById(documento)
                .map(usuario -> {
                    usuario.setDocumento(usuarioActualizado.getDocumento());
                    return repositorio.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("No se pudo actualizar, ID no existe"));
    }

    @DeleteMapping("/{documento}")
    public ResponseEntity<?> eliminar(@PathVariable String documento){
        try {
            repositorio.deleteByDocumento(documento);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }

    
}
