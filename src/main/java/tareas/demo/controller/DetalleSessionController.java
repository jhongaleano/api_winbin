package tareas.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import tareas.demo.models.DetalleSession;
import tareas.demo.repository.DetalleSessionRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/DetalleSession")
public class DetalleSessionController {

    private final DetalleSessionRepository repositorio;
    
    public DetalleSessionController(DetalleSessionRepository repositorio){
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<DetalleSession> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    public DetalleSession crear(@RequestBody DetalleSession nuevo) {
        return repositorio.save(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleSession> actualizar(@PathVariable UUID id, @RequestBody DetalleSession cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setFechaHora(cambios.getFechaHora());
            existente.setDocumento(cambios.getDocumento());
            existente.setId_categoria(cambios.getId_categoria());
            existente.setId_periodo(cambios.getId_periodo());
            DetalleSession actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
