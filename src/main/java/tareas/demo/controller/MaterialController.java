package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.Material;
import tareas.demo.repository.MaterialRepository;

@RestController
@RequestMapping("/api/materiales")
@Tag(name = "Materiales", description = "Tipos de material reciclable (plástico, vidrio, etc.) vinculados a recursos multimedia")
public class MaterialController {

    private final MaterialRepository repositorio;

    public MaterialController(MaterialRepository repository) {
        this.repositorio = repository;
    }

    @Operation(summary = "Listar materiales", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<Material> listar() {
        return repositorio.findAll();
    }

    @Operation(summary = "Crear material", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping
    public Material crear(@RequestBody Material nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(summary = "Eliminar material", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Material> actualizar(@PathVariable Integer id, @RequestBody Material cambios) {
        return repositorio.findById(id).map(existente -> {
            if(cambios.getNombreMaterial() != null){
                existente.setNombreMaterial(cambios.getNombreMaterial());
            }
            if(cambios.getRecursosMultimedia() != null){
                existente.setRecursosMultimedia(cambios.getRecursosMultimedia());
            }
            Material actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
