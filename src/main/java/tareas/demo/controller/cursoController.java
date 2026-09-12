package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.Cursos;
import tareas.demo.repository.CursoRepository;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "Cursos", description = "CRUD de cursos académicos y puntos totales acumulados")
public class cursoController {

    private final CursoRepository repositorio;

    public cursoController(CursoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Operation(summary = "Listar cursos", description = "**Público** — no requiere JWT.")
    @GetMapping
    public List<Cursos> listar() {
        return repositorio.findAll();
    }

    @Operation(summary = "Crear curso", description = "Solo ADMIN + JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping
    public Cursos crear(@RequestBody Cursos nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(summary = "Eliminar curso", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar curso", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{id}")
    public ResponseEntity<Cursos> actualizar(@PathVariable Integer id, @RequestBody Cursos cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setNombreCurso(cambios.getNombreCurso());
            existente.setPuntosTotales(cambios.getPuntosTotales());
            return ResponseEntity.ok(repositorio.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }
}
