package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.PeriodoRanking;
import tareas.demo.repository.PeriodoRankingRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/PeriodoRanking")
public class PeriodoRankingController {

    @Autowired
    private PeriodoRankingRepository repositorio;

    @GetMapping
    public List<PeriodoRanking> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    public PeriodoRanking crear(@RequestBody PeriodoRanking nuevo) {
        return repositorio.save(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodoRanking> actualizar(@PathVariable Long id, @RequestBody PeriodoRanking cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setNombrePeriodo(cambios.getNombrePeriodo());
            existente.setFechaInicio(cambios.getFechaInicio());
            existente.setFechaFin(cambios.getFechaFin());
            existente.setActivo(cambios.getActivo());
            PeriodoRanking actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
