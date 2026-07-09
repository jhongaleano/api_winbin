package tareas.demo.controller;

import tareas.demo.models.Material;
import tareas.demo.repository.MaterialRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/materiales")

public class MaterialController {


    private final MaterialRepository repositorio ;

    public MaterialController(MaterialRepository repository) {
        this.repositorio = repository;
    }

    @GetMapping
    public List<Material> listar(){
        return repositorio.findAll();
    }

    @PostMapping
    public Material crear(@RequestBody Material nuevo) {
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
    public ResponseEntity<Material> actualizar(@PathVariable Integer id, @RequestBody Material cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setNombreMaterial(cambios.getNombreMaterial());
            existente.setRecursosMultimedia(cambios.getRecursosMultimedia());
            Material actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
    
}
