package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.RecursosMultimedia;
import tareas.demo.repository.RecursosMultimediaRepository;

@RestController
@RequestMapping("/api/RecursosMultimedia")
@Tag(name = "Recursos multimedia", description = "Recursos educativos (videos, imágenes, documentos) asociados a materiales")
public class RecursosMultimediaController {

    @Autowired
    private RecursosMultimediaRepository repositorio;

    @Operation(summary = "Listar recursos", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<RecursosMultimedia> listar() {
        return repositorio.findAll();
    }

    @Operation(summary = "Crear recurso", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping
    public RecursosMultimedia crear(@RequestBody RecursosMultimedia nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(summary = "Eliminar recurso", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar recurso", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PutMapping("/{id}")
    public ResponseEntity<RecursosMultimedia> actualizar(@PathVariable Integer id, @RequestBody RecursosMultimedia cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setTipoRecurso(cambios.getTipoRecurso());
            existente.setCategoria(cambios.getCategoria());
            existente.setContenido(cambios.getContenido());
            existente.setUrlArchivo(cambios.getUrlArchivo());
            existente.setEstado(cambios.getEstado());
            return ResponseEntity.ok(repositorio.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }
}
