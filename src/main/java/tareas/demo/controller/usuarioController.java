package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.dto.CambiarPasswordDTO;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;
import tareas.demo.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios, perfiles, roles, avatares y contraseñas")
public class usuarioController {

    private final UsuarioRepository repositorio;
    private final UsuarioService usuarioService;

    public usuarioController(UsuarioRepository repositorio, UsuarioService usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar usuarios activos", description = "Retorna todos los usuarios con `activo = true`. Requiere JWT (USER/ESTUDIANTE/ADMIN).",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<usuarios> listar() {
        return repositorio.findAll();
    }

    @Operation(summary = "Listar usuarios inactivos", description = "Usuarios desactivados (soft delete). Requiere JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("/inactivos")
    public ResponseEntity<List<usuarios>> listarInactivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosInactivos());
    }

    @Operation(summary = "Listar todos los usuarios", description = "Incluye activos e inactivos. Requiere JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("/todos")
    public ResponseEntity<List<usuarios>> listarTodosInactivosYActivos() {
        return ResponseEntity.ok(usuarioService.listarTodosIncluyendoInactivos());
    }

    @Operation(summary = "Obtener mi perfil", description = "Perfil del usuario autenticado según el JWT. Requiere JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerMiPerfil(Authentication authentication) {
        try {
            String documentoUsuarioLogueado = authentication.getName();
            usuarios perfil = usuarioService.obtenerPerfil(documentoUsuarioLogueado);
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el perfil: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener usuario por documento", description = "Busca un usuario por su documento. Requiere JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("/{documento}")
    public ResponseEntity<?> obtenerUsuarioPorDocumento(
            @Parameter(description = "Documento del usuario", example = "1234567890") @PathVariable String documento) {
        try {
            usuarios usuario = usuarioService.obtenerPerfil(documento);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Usuario no encontrado: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar avatar", description = "Actualiza la URL del avatar del usuario autenticado. Requiere rol ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PatchMapping("/avatar")
    public ResponseEntity<?> actualizarAvatar(
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        try {
            String documento = authentication.getName();
            String nuevaUrl = body.get("avatarUrl");

            if (nuevaUrl == null || nuevaUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'avatarUrl' es obligatorio.");
            }

            usuarioService.actualizarAvatar(documento, nuevaUrl);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Avatar actualizado con éxito",
                    "avatarUrl", nuevaUrl));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar avatar: " + e.getMessage());
        }
    }

    @Operation(summary = "Registrar usuario", description = """
            Registro público de nuevos estudiantes. La contraseña se cifra con BCrypt.
            El rol se asigna automáticamente como `ROLE_USER`. No requiere JWT.
            """)
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody usuarios nuevoUsuario) {
        try {
            usuarios usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
            return ResponseEntity.ok(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza nombre y curso. Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{documento}")
    public ResponseEntity<?> actualizar(
            @PathVariable String documento,
            @RequestBody usuarios usuarioActualizado) {
        try {
            usuarios usuarioModificado = repositorio.findById(documento)
                    .map(usuario -> {
                        if (usuarioActualizado.getNombre() != null) {
                            usuario.setNombre(usuarioActualizado.getNombre());
                        }
                        if (usuarioActualizado.getCurso() != null) {
                            usuario.setCurso(usuarioActualizado.getCurso());
                        }
                        return repositorio.save(usuario);
                    })
                    .orElseThrow(() -> new RuntimeException("El usuario con documento " + documento + " no existe"));
            return ResponseEntity.ok(usuarioModificado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }

    @Operation(summary = "Cambiar rol de usuario", description = "Asigna un nuevo rol (ej: ADMIN, USER). Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{documento}/rol")
    public ResponseEntity<?> cambiarRol(
            @PathVariable String documento,
            @RequestBody Map<String, String> requestBody) {
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

    @Operation(summary = "Reactivar usuario", description = "Reactiva un usuario previamente desactivado. Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{documento}/reactivar")
    public ResponseEntity<?> reactivarUsuario(@PathVariable String documento) {
        try {
            usuarios usuarioReactivado = usuarioService.reactivarUsuario(documento);
            return ResponseEntity.ok(usuarioReactivado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al reactivar usuario: " + e.getMessage());
        }
    }

    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña del usuario autenticado. Valida la contraseña actual. Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/perfil/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody CambiarPasswordDTO dto) {
        try {
            usuarioService.cambiarPasswordUsuarioAutenticado(dto);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Desactivar usuario", description = "Soft delete: marca `activo = false`. Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{documento}")
    public ResponseEntity<?> eliminar(@PathVariable String documento) {
        try {
            usuarioService.eliminarUsuario(documento);
            return ResponseEntity.ok("Usuario eliminado/desactivado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }
}
