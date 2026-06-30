package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.RecursosMultimedia;
import tareas.demo.repository.RecursosMultimediaRepository;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/RecursosMultimedia")
public class RecursosMultimediaController {

    @Autowired
    private RecursosMultimediaRepository repositorio;

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

    @PutMapping("/{id}")
    public ResponseEntity<RecursosMultimedia> actualizar(@PathVariable Integer id, @RequestBody RecursosMultimedia cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setTipoRecurso(cambios.getTipoRecurso());
            existente.setCategoria(cambios.getCategoria());
            existente.setContenido(cambios.getContenido());
            existente.setUrlArchivo(cambios.getUrlArchivo());
            existente.setEstado(cambios.getEstado());
            RecursosMultimedia actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
    


}
