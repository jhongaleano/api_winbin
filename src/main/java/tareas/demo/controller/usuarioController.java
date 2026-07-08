package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.usuarios;
import tareas.demo.payload.LoginRequest;
import tareas.demo.repository.UsuarioRepository;
import tareas.demo.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class usuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repositorio;

    @GetMapping
    public List<usuarios> listar() {
        return repositorio.findAll();
    }

    @Autowired
    private UsuarioService usuarioService;

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
    public usuarios actualizar(
        @PathVariable String documento,
        @RequestBody usuarios usuarioActualizado
    ) {
        return repositorio
            .findById(documento)
            .map(usuario -> {
                usuario.setDocumento(usuarioActualizado.getDocumento());
                return repositorio.save(usuario);
            })
            .orElseThrow(() ->
                new RuntimeException("No se pudo actualizar, ID no existe")
            );
    }

    @DeleteMapping("/{documento}")
    public ResponseEntity<?> eliminar(@PathVariable String documento) {
        try {
            repositorio.deleteByDocumento(documento);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "Error al eliminar: " + e.getMessage()
            );
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Spring Security usará internamente tu CustomUserDetailsService aquí:
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getDocumento(), // <--- Aquí va el documento
                loginRequest.getContrasenna()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Retornas tu token JWT o mensaje de éxito
        return ResponseEntity.ok("Login exitoso");
    }
}
