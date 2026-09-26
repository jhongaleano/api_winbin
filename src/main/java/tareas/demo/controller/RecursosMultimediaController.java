package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.RecursosMultimedia;
import tareas.demo.repository.RecursosMultimediaRepository;

@RestController
@RequestMapping("/api/RecursosMultimedia")
@Tag(name = "Recursos multimedia", description = "Recursos educativos (videos, imágenes, documentos) asociados a materiales")
public class RecursosMultimediaController {

    private final RecursosMultimediaRepository repositorio;

    public RecursosMultimediaController(RecursosMultimediaRepository repositorio) {
        this.repositorio = repositorio;
    }

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

    @PatchMapping("/{id}")
    public ResponseEntity<RecursosMultimedia> actualizar(@PathVariable Integer id, @RequestBody RecursosMultimedia cambios) {
        return repositorio.findById(id).map(existente -> {
            if(cambios.getTipoRecurso() != null){
                existente.setTipoRecurso(cambios.getTipoRecurso());
            }
            if(cambios.getCategoria() != null){
                existente.setCategoria(cambios.getCategoria());
            }
            if(cambios.getContenido() != null){
                existente.setContenido(cambios.getContenido());
            }
            if(cambios.getUrlArchivo() != null){
                existente.setUrlArchivo(cambios.getUrlArchivo());
            }
            if(cambios.getEstado() != null){
                existente.setEstado(cambios.getEstado());
            }
            RecursosMultimedia actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
