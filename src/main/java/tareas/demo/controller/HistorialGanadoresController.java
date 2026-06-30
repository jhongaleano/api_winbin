package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.HistorialGanadores;
import tareas.demo.repository.HistorialGanadoresRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/HistorialGanadores")
public class HistorialGanadoresController {

    @Autowired
    private HistorialGanadoresRepository repositorio;

    @GetMapping
    public List<HistorialGanadores> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    public HistorialGanadores crear(@RequestBody HistorialGanadores nuevo) {
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
    public ResponseEntity<HistorialGanadores> actualizar(@PathVariable Integer id, @RequestBody HistorialGanadores cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setPuesto(cambios.getPuesto());
            existente.setTipoPremio(cambios.getTipoPremio());
            existente.setPuntosLogrados(cambios.getPuntosLogrados());
            existente.setFecha(cambios.getFecha());
            existente.setPremioDado(cambios.getPremioDado());
            existente.setDocumento(cambios.getDocumento());
            existente.setId_curso(cambios.getId_curso());
            existente.setId_periodo(cambios.getId_periodo());
            HistorialGanadores actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
