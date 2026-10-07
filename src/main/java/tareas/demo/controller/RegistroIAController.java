package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.models.DetalleSession;
import tareas.demo.models.Material;
import tareas.demo.models.RegistroIa;
import tareas.demo.repository.CategoriaPuntajeRepository;
import tareas.demo.repository.DetalleSessionRepository;
import tareas.demo.repository.MaterialRepository;
import tareas.demo.repository.RegistroIaRepository;
import tareas.demo.services.RegistroIaService;

@RestController
@RequestMapping("/api/registroia")
@Tag(name = "Registro IA", description = "Resultados de clasificación del servicio de inteligencia artificial")
public class RegistroIAController {

    private final RegistroIaService registroIaService ;
    private final RegistroIaRepository repositorio ;

    public RegistroIAController(RegistroIaRepository repositorio, RegistroIaService registroIaService) {
        this.repositorio = repositorio;
        this.registroIaService = registroIaService;
    }

    @Operation(summary = "Listar registros de IA", description = "Solo ADMIN. Parámetros: `?page=0&size=20&sort=idIa,desc`", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RegistroIa> listar(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Operation(summary = "Guardar resultado de la IA", description = """
            Recibe el resultado de clasificación desde **Python**, Flutter o cualquier cliente autenticado.

            **Campos obligatorios:** `idSession`, `idMaterial`, `idCategoria`.

            **Ejemplo body:**
            ```json
            {
              "idSession": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
              "idMaterial": 1,
              "idCategoria": 2,
              "confianza": 0.95,
              "utlImagen": "https://storage.ejemplo.com/foto.jpg"
            }
            ```

            Reutiliza el **mismo** `idSession` para múltiples capturas en una ronda.
            Requiere JWT (cualquier rol autenticado).
            """, security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping("/guardar-resultado")
    public ResponseEntity<?> guardarDesdePython(@RequestBody RegistroIa nuevoRegistro) {
        if (nuevoRegistro.getIdSession() == null || nuevoRegistro.getIdMaterial() == null) {
            return ResponseEntity.badRequest().body("Faltan IDs requeridos");
        }
        try {
            RegistroIa guardado = registroIaService.guardarResultadoIA(nuevoRegistro);
            return ResponseEntity.ok(guardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar registro de IA", description = "Solo ADMIN + JWT.", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del registro IA") @PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar registro de IA", description = "Solo ADMIN + JWT.", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RegistroIa cambios) {
        try {
            // El controlador solo delega el trabajo al servicio
            RegistroIa actualizado = registroIaService.actualizarResultadoIA(id, cambios);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
