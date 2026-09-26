package tareas.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.HistorialGanadores;
import tareas.demo.repository.HistorialGanadoresRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/HistorialGanadores")
public class HistorialGanadoresController {

    private final HistorialGanadoresRepository repositorio;

    public HistorialGanadoresController(HistorialGanadoresRepository repositorio){
        this.repositorio = repositorio;
    }

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

    @PatchMapping("/{id}")
    public ResponseEntity<HistorialGanadores> actualizar(@PathVariable Integer id, @RequestBody HistorialGanadores cambios) {
        return repositorio.findById(id).map(existente -> {
            if(cambios.getPuesto() != null){
            existente.setPuesto(cambios.getPuesto());
            }
            if(cambios.getTipoPremio() != null){
            existente.setTipoPremio(cambios.getTipoPremio());
            }
            if(cambios.getPuntosLogrados() != null){
            existente.setPuntosLogrados(cambios.getPuntosLogrados());
            }
            if(cambios.getFecha() != null){
            existente.setFecha(cambios.getFecha());
            }
            if(cambios.getPremioDado() != null){
            existente.setPremioDado(cambios.getPremioDado());
            }
            if(cambios.getDocumento() != null){
            existente.setDocumento(cambios.getDocumento());
            }
            if(cambios.getId_curso() != null){
            existente.setId_curso(cambios.getId_curso());
            }
            if(cambios.getId_periodo() != null){
            existente.setId_periodo(cambios.getId_periodo());
            }
            HistorialGanadores actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
