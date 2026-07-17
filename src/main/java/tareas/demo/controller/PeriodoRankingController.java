package tareas.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tareas.demo.models.PeriodoRanking;
import tareas.demo.repository.PeriodoRankingRepository;
import tareas.demo.services.PeriodoRankingService;

import java.util.Map;

@RestController
@RequestMapping("/api/PeriodoRanking")
public class PeriodoRankingController {

    
    private final PeriodoRankingRepository repositorio;
    private final PeriodoRankingService periodoService;

    public PeriodoRankingController(PeriodoRankingRepository repositorio, PeriodoRankingService periodoService){
        this.repositorio = repositorio;
        this.periodoService = periodoService;
    }

    @GetMapping
    public List<PeriodoRanking> listar() {
        return repositorio.findAll();
    }


    @GetMapping("/activo")
    public ResponseEntity<?> obtenerPeriodoActivo() {
        return periodoService.obtenerOActualizarPeriodoActivo()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("mensaje", "No hay ningún período de ranking activo actualmente")));
    }
    

    @PostMapping
    public PeriodoRanking crear(@RequestBody PeriodoRanking nuevo) {
        return repositorio.save(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodoRanking> actualizar(@PathVariable String id, @RequestBody PeriodoRanking cambios) {
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
