package tareas.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;
import tareas.demo.services.UsuarioService;
import org.springframework.security.core.Authentication;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class usuarioController {

    private  final UsuarioRepository repositorio;
    private  final UsuarioService usuarioService;

    public usuarioController(UsuarioRepository repositorio, UsuarioService usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<usuarios> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<usuarios>> listarInactivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosInactivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<usuarios>> listarTodosInactivosYActivos() {
        return ResponseEntity.ok(usuarioService.listarTodosIncluyendoInactivos());
    }

    @GetMapping("/sr")
    public ResponseEntity<?> obtenerMiPerfil(Authentication authentication) {
        try {
            String documentoUsuarioLogueado = authentication.getName(); 
            
            usuarios perfil = usuarioService.obtenerPerfil(documentoUsuarioLogueado);
            return ResponseEntity.ok(perfil);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el perfil: " + e.getMessage());
        }
    }

    @GetMapping("/{documento}")
    public ResponseEntity<?> obtenerUsuarioPorDocumento(@PathVariable String documento) {
        try {
            usuarios usuario = usuarioService.obtenerPerfil(documento);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Usuario no encontrado: " + e.getMessage());
        }
    }

    @PatchMapping("/avatar")
    public ResponseEntity<?> actualizarAvatar(
        @RequestBody Map<String, String> body,
        Authentication authentication
    ) {
        try {

            String documento = authentication.getName(); 
        
            String nuevaUrl = body.get("avatarUrl");

            if (nuevaUrl == null || nuevaUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'avatarUrl' es obligatorio.");
            }

            usuarioService.actualizarAvatar(documento, nuevaUrl);
            

            return ResponseEntity.ok(Map.of(
            "mensaje", "Avatar actualizado con éxito",
            "avatarUrl", nuevaUrl
        ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar avatar: " + e.getMessage());
        }
    }
    

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody usuarios nuevoUsuario) {
        try {
            usuarios usuarioGuardado = usuarioService.guardarUsuario(
                nuevoUsuario
            );
            return ResponseEntity.ok(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "Error al registrar: " + e.getMessage()
            );
        }
    }

    @PutMapping("/{documento}")
    public ResponseEntity<?> actualizar(
        @PathVariable String documento,
        @RequestBody usuarios usuarioActualizado
    ) {
        try{
            usuarios usuarioModificado = repositorio.findById(documento)
            .map(usuario -> {
                usuario.setNombre(usuarioActualizado.getNombre());
                usuario.setCurso(usuarioActualizado.getCurso());
                return repositorio.save(usuario);
            })
            .orElseThrow(() -> 
                new RuntimeException("El usuario con documento " + documento + " no existe")
            );
            return ResponseEntity.ok(usuarioModificado);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "Error al actualizar: " + e.getMessage()
            );
        }
    }
    
    @PutMapping("/{documento}/rol")
    public ResponseEntity<?> cambiarRol(
            @PathVariable String documento,
            @RequestBody Map<String, String> requestBody
    ) {
        try {
            String nuevoRol = requestBody.get("rol");
            if (nuevoRol == null || nuevoRol.isBlank()) {
                return ResponseEntity.badRequest().body("El campo 'rol' es obligatorio.");
            }

            usuarios usuarioActualizado = usuarioService.cambiarRolUsuario(documento, nuevoRol);
            return ResponseEntity.ok(usuarioActualizado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar rol: " + e.getMessage());
        }
    }

    @PutMapping("/{documento}/reactivar")
    public ResponseEntity<?> reactivarUsuario(@PathVariable String documento) {
        try {
            usuarios usuarioReactivado = usuarioService.reactivarUsuario(documento);
            return ResponseEntity.ok(usuarioReactivado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al reactivar usuario: " + e.getMessage());
        }
    }


    @DeleteMapping("/{documento}")
    public ResponseEntity<?> eliminar(@PathVariable String documento) {
        try {
            usuarioService.eliminarUsuario(documento);
            return ResponseEntity.ok("Usuario eliminado/desactivado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "Error al eliminar: " + e.getMessage()
            );
        }
    }

}
