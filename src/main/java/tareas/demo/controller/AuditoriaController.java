package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.Auditoria;
import tareas.demo.repository.AuditoriaRepository;

@RestController
@RequestMapping("/api/auditoria")
@Tag(name = "Auditoría", description = "Registro de INSERT/UPDATE/DELETE en el sistema. Notificaciones en tiempo real vía WebSocket `/topic/auditoria`")
public class AuditoriaController {

    private final AuditoriaRepository repositorio;

    public AuditoriaController(AuditoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Operation(
            summary = "Listar auditoría (paginado)",
            description = """
                    Solo ADMIN. Parámetros: `?page=0&size=20&sort=fecha,desc`

                    Eventos en tiempo real disponibles en WebSocket: `wss://api-winbin.onrender.com/ws` → `/topic/auditoria`
                    """,
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Auditoria> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }
}
