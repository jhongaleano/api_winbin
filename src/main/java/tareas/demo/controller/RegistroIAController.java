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

@RestController
@RequestMapping("/api/registroia")
@Tag(name = "Registro IA", description = "Resultados de clasificación del servicio de inteligencia artificial")
public class RegistroIAController {

    private final RegistroIaRepository repositorio;
    private final DetalleSessionRepository sessionRepository;
    private final MaterialRepository materialRepository;
    private final CategoriaPuntajeRepository categoriaRepository;

    public RegistroIAController(
            RegistroIaRepository repository,
            DetalleSessionRepository sessionRepository,
            MaterialRepository materialRepository,
            CategoriaPuntajeRepository categoriaRepository) {
        this.repositorio = repository;
        this.sessionRepository = sessionRepository;
        this.materialRepository = materialRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Operation(
            summary = "Listar registros de IA (paginado)",
            description = "Solo ADMIN. Parámetros: `?page=0&size=20&sort=idIa,desc`",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RegistroIa> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Operation(
            summary = "Guardar resultado de la IA",
            description = """
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
                    """,
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @PostMapping("/guardar-resultado")
    public ResponseEntity<?> guardarDesdePython(@RequestBody RegistroIa nuevoRegistro) {
        if (nuevoRegistro.getIdSession() == null || nuevoRegistro.getIdMaterial() == null) {
            return ResponseEntity.badRequest().body("Faltan IDs requeridos");
        }
        DetalleSession sesion = sessionRepository.findById(nuevoRegistro.getIdSession())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Material mat = materialRepository.findById(nuevoRegistro.getIdMaterial())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        CategoriaPuntaje cat = categoriaRepository.findById(nuevoRegistro.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        nuevoRegistro.setSession(sesion);
        nuevoRegistro.setMaterial(mat);
        nuevoRegistro.setCategoria(cat);

        return ResponseEntity.ok(repositorio.save(nuevoRegistro));
    }

    @Operation(summary = "Eliminar registro de IA", description = "Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del registro IA") @PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar registro de IA", description = "Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{id}")
    public ResponseEntity<RegistroIa> actualizar(@PathVariable Long id, @RequestBody RegistroIa cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setConfianza(cambios.getConfianza());
            existente.setUtlImagen(cambios.getUtlImagen());
            existente.setIdSession(cambios.getIdSession());
            existente.setMaterial(cambios.getMaterial());
            return ResponseEntity.ok(repositorio.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }
}
