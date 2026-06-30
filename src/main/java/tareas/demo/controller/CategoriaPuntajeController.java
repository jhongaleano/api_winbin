package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.repository.CategoriaPuntajeRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/CategoriaPuntaje")
public class CategoriaPuntajeController {

    @Autowired
    private CategoriaPuntajeRepository repositorio;

    @GetMapping
    public List<CategoriaPuntaje> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    public CategoriaPuntaje crear(@RequestBody CategoriaPuntaje nuevo) {
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
    public ResponseEntity<CategoriaPuntaje> actualizar(@PathVariable Integer id, @RequestBody CategoriaPuntaje cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setTamanno(cambios.getTamanno());
            existente.setPuntos(cambios.getPuntos());
            existente.setId_material(cambios.getId_material());
            CategoriaPuntaje actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

}
