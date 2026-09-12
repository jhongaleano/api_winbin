package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.DetalleSession;
import tareas.demo.repository.DetalleSessionRepository;

@RestController
@RequestMapping("/api/DetalleSession")
@Tag(name = "Sesiones", description = """
        Sesiones de clasificación/reciclaje. Cada sesión tiene un `id_session` (UUID) que agrupa \
        múltiples capturas de imagen enviadas a la IA durante una misma ronda.
        """)
public class DetalleSessionController {

    private final DetalleSessionRepository repositorio;

    public DetalleSessionController(DetalleSessionRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Operation(summary = "Listar sesiones", description = "Lista todas las sesiones registradas. Requiere JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<DetalleSession> listar() {
        return repositorio.findAll();
    }

    @Operation(
            summary = "Crear sesión de clasificación",
            description = """
                    Crea una nueva sesión de trabajo. **Llamar una sola vez** al iniciar una ronda de capturas.

                    El `id_session` (UUID) generado debe guardarse en el cliente y reutilizarse en cada foto.

                    **Ejemplo body:**
                    ```json
                    {
                      "documento": { "documento": "1234567890" },
                      "id_periodo": { "id_periodo": "PER-2026-03-123456" },
                      "id_categoria": { "id_categoria": 1 }
                    }
                    ```
                    Solo ADMIN + JWT.
                    """,
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @PostMapping
    public DetalleSession crear(@RequestBody DetalleSession nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(summary = "Eliminar sesión", description = "Elimina una sesión por UUID. Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "UUID de la sesión", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable UUID id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar sesión", description = "Actualiza fecha, usuario, categoría o periodo. Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{id}")
    public ResponseEntity<DetalleSession> actualizar(@PathVariable UUID id, @RequestBody DetalleSession cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setFechaHora(cambios.getFechaHora());
            existente.setDocumento(cambios.getDocumento());
            existente.setId_categoria(cambios.getId_categoria());
            existente.setId_periodo(cambios.getId_periodo());
            return ResponseEntity.ok(repositorio.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }
}
