package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.models.Cursos;
import tareas.demo.models.usuarios;
import tareas.demo.services.CursosService;
import tareas.demo.services.UsuarioService;

@RestController
@RequestMapping("/api/ranking")
@Tag(name = "Ranking", description = "Rankings de estudiantes y cursos ordenados por puntos acumulados")
public class RankingController {

    private final UsuarioService usuarioService;
    private final CursosService cursosService;

    public RankingController(UsuarioService usuarioService, CursosService cursosService) {
        this.usuarioService = usuarioService;
        this.cursosService = cursosService;
    }

    @Operation(
            summary = "Ranking general de estudiantes",
            description = "Top 10 usuarios con más puntos. Requiere JWT (USER/ESTUDIANTE/ADMIN).",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    )
    @GetMapping("/top10/estudiantes")
    public ResponseEntity<List<usuarios>> getRankingGeneral() {
        return ResponseEntity.ok(usuarioService.obtenerTop10Estudiantes());
    }

    @Operation(
            summary = "Usuario con más puntos",
            description = "Retorna el estudiante #1 del ranking. **Público** — no requiere JWT."
    )
    @GetMapping("/top-usuario")
    public ResponseEntity<?> getTopUsuario() {
        usuarios topUser = usuarioService.obtenerUsuarioTOP();
        if (topUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topUser);
    }

    @Operation(summary = "Obtener usuarios paginados", description = "Obtiene los usuarios paginados.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("usuarios/paginado")
    public ResponseEntity<List<usuarios>> obtenerPaginados(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(usuarioService.obtenerUsuariosPaginados(page));
    }

   

    @Operation(summary = "Obtener top 10 cursos", description = "Obtiene los top 10 cursos.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("top10/cursos")
    public ResponseEntity<List<Cursos>> getRankingCursos() {
        return ResponseEntity.ok(cursosService.obtenerTop10Cursos());
    }



    @Operation(
            summary = "Curso con más puntos",
            description = "Retorna el curso #1 del ranking. **Público** — no requiere JWT."
    )
    @GetMapping("/top-curso")
    public ResponseEntity<?> getTopCurso() {
        Cursos topCurso = cursosService.obtenerCursoTop();
        if (topCurso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topCurso);
    }

    @Operation(summary = "Obtener cursos paginados", description = "Obtiene los cursos paginados.",
            security = @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH))
    @GetMapping("cursos/paginado")
    public ResponseEntity<List<Cursos>> obtenerPaginadosCursos(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(cursosService.obtenerCursosPaginados(page));
    }
}
