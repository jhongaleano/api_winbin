package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.HistorialGanadores;
import tareas.demo.repository.HistorialGanadoresRepository;

@RestController
@RequestMapping("/api/HistorialGanadores")
@Tag(name = "Historial de ganadores", description = "Registro de ganadores individuales (INDIVIDUAL) y por curso (CURSO) al cerrar un periodo")
public class HistorialGanadoresController {

    private final HistorialGanadoresRepository repositorio;

    public HistorialGanadoresController(HistorialGanadoresRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Operation(summary = "Listar historial", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<HistorialGanadores> listar() {
        return repositorio.findAll();
    }

    @Operation(summary = "Crear registro de historial", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping
    public HistorialGanadores crear(@RequestBody HistorialGanadores nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(summary = "Eliminar registro", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
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
