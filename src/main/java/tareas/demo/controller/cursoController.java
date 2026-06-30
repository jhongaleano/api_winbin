package tareas.demo.controller;
import tareas.demo.models.Cursos;
import tareas.demo.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/cursos")

public class cursoController{
    @Autowired
    private CursoRepository repositorio;

    @GetMapping
    public List<Cursos> listar(){
        return repositorio.findAll();
    }

    @PostMapping
    public Cursos crear(@RequestBody Cursos nuevo) {
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
    public ResponseEntity<Cursos> actualizar(@PathVariable Integer id, @RequestBody Cursos cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setNombreCurso(cambios.getNombreCurso());
            existente.setPuntosTotales(cambios.getPuntosTotales());
            Cursos actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
    
}