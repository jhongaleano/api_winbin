package tareas.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.RecursosMultimedia;
import tareas.demo.repository.RecursosMultimediaRepository;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/RecursosMultimedia")
public class RecursosMultimediaController {

    private final RecursosMultimediaRepository repositorio;

    public RecursosMultimediaController(RecursosMultimediaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<RecursosMultimedia> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    public RecursosMultimedia crear(@RequestBody RecursosMultimedia nuevo) {
        return repositorio.save(nuevo);
    }

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
