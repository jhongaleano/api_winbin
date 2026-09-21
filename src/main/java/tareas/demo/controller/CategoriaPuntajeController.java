package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.repository.CategoriaPuntajeRepository;

@RestController
@RequestMapping("/api/CategoriaPuntaje")
@Tag(name = "Categorías de puntaje", description = "Puntos otorgados según tamaño del material: pequeño, mediano, grande")
public class CategoriaPuntajeController {

    @Autowired
    private CategoriaPuntajeRepository repositorio;

    @Operation(summary = "Listar categorías", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<CategoriaPuntaje> listar() {
        return repositorio.findAll();
    }

    @Operation(summary = "Crear categoría", description = "Valores de `tamanno`: pequeño, mediano, grande.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping
    public CategoriaPuntaje crear(@RequestBody CategoriaPuntaje nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(summary = "Eliminar categoría", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar categoría", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaPuntaje> actualizar(@PathVariable Integer id, @RequestBody CategoriaPuntaje cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setTamanno(cambios.getTamanno());
            existente.setPuntos(cambios.getPuntos());
            existente.setId_material(cambios.getId_material());
            return ResponseEntity.ok(repositorio.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }
}
