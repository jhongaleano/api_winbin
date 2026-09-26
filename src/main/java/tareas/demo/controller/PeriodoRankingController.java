package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tareas.demo.models.PeriodoRanking;
import tareas.demo.repository.PeriodoRankingRepository;
import tareas.demo.services.CierrePeriodoService;
import tareas.demo.services.PeriodoRankingService;

@RestController
@RequestMapping("/api/PeriodoRanking")
@Tag(name = "Periodos de ranking", description = "Periodos de competencia con fechas, activación automática y cierre con reinicio de puntos")
public class PeriodoRankingController {

    private final PeriodoRankingRepository repositorio;
    private final PeriodoRankingService periodoService;
    private final CierrePeriodoService cierrePeriodoService;

    public PeriodoRankingController(
            PeriodoRankingRepository repositorio,
            PeriodoRankingService periodoService,
            CierrePeriodoService cierrePeriodoService) {
        this.repositorio = repositorio;
        this.periodoService = periodoService;
        this.cierrePeriodoService = cierrePeriodoService;
    }

    @Operation(summary = "Listar periodos", description = "Todos los periodos de ranking. Requiere JWT.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping
    public List<PeriodoRanking> listar() {
        return repositorio.findAll();
    }

    @Operation(
            summary = "Obtener periodo activo",
            description = """
                    Retorna el periodo vigente. Si la fecha actual está fuera del rango, desactiva el periodo \
                    y busca uno válido automáticamente. Responde 404 si no hay periodo activo.
                    Requiere JWT.
                    """,
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @GetMapping("/activo")
    public ResponseEntity<?> obtenerPeriodoActivo() {
        return periodoService.obtenerOActualizarPeriodoActivo()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("mensaje", "No hay ningún período de ranking activo actualmente")));
    }

    @Operation(summary = "Crear periodo", description = "El `id_periodo` se genera automáticamente si no se envía (formato PER-YYYY-MM-XXXXXX). Solo ADMIN.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @PostMapping
    public PeriodoRanking crear(@RequestBody PeriodoRanking nuevo) {
        return repositorio.save(nuevo);
    }

    @Operation(
            summary = "Cerrar periodo manualmente",
            description = """
                    Guarda top 10 usuarios y cursos en historial de ganadores, reinicia puntos a 0 y desactiva el periodo.
                    Solo ADMIN + JWT.
                    """,
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @PostMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarPeriodoManualmente(@PathVariable String id) {
        PeriodoRanking periodo = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Período no encontrado"));

        cierrePeriodoService.ejecutarCierreYReiniciarPuntos(periodo);

        periodo.setActivo(false);
        repositorio.save(periodo);

        return ResponseEntity.ok(Map.of("mensaje", "Período cerrado, ganadores guardados y puntos reiniciados a 0 con éxito."));
    }

    @Operation(summary = "Eliminar periodo", security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PeriodoRanking> actualizar(@PathVariable String id, @RequestBody PeriodoRanking cambios) {
        return repositorio.findById(id).map(existente -> {
            if(cambios.getNombrePeriodo() != null){
                existente.setNombrePeriodo(cambios.getNombrePeriodo());
            }
            if(cambios.getFechaInicio() != null){
                existente.setFechaInicio(cambios.getFechaInicio());
            }
            if(cambios.getFechaFin() != null){
                existente.setFechaFin(cambios.getFechaFin());
            }
            if(cambios.getActivo() != null){
                existente.setActivo(cambios.getActivo());
            }
            PeriodoRanking actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
