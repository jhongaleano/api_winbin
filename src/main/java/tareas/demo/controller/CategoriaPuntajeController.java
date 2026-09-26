package tareas.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.repository.CategoriaPuntajeRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/CategoriaPuntaje")
public class CategoriaPuntajeController {

    private final CategoriaPuntajeRepository repositorio;

    public CategoriaPuntajeController(CategoriaPuntajeRepository repositorio) {
        this.repositorio = repositorio;
    }

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
    

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaPuntaje> actualizar(@PathVariable Integer id, @RequestBody CategoriaPuntaje cambios) {
        return repositorio.findById(id).map(existente -> {
            if(cambios.getTamanno() != null){
                existente.setTamanno(cambios.getTamanno());
            }
            if(cambios.getPuntos() != null){
                existente.setPuntos(cambios.getPuntos());
            }
            if(cambios.getId_material() != null){
                existente.setId_material(cambios.getId_material());
            }
            CategoriaPuntaje actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

}
