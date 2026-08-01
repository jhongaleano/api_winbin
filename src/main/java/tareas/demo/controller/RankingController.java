package tareas.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.Cursos;
import tareas.demo.models.usuarios;
import tareas.demo.services.*;

@RestController
@RequestMapping("/api/ranking") // Permite que Flutter se conecte sin problemas de CORS
public class RankingController {

    
    private UsuarioService usuarioService;

    
    private CursosService cursosService;

    
    public RankingController(UsuarioService usuarioService, CursosService cursosService) {
        this.usuarioService = usuarioService;
        this.cursosService = cursosService;
    }

    /**
     * Endpoint para obtener el Ranking General de Usuarios
     * URL: GET http://localhost:8080/api/ranking/general
     */
    @GetMapping("/general")
    public ResponseEntity<List<usuarios>> getRankingGeneral() {
        List<usuarios> ranking = usuarioService.obtenerTop10Estudiantes();
        return ResponseEntity.ok(ranking);
    }


    @GetMapping("/top-usuario")
    public ResponseEntity<?> getTopUsuario() {
        usuarios topUser = usuarioService.obtenerUsuarioTOP();
        if (topUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topUser);
    }

    /**
     * Endpoint para obtener el Ranking por Cursos
     * URL: GET http://localhost:8080/api/ranking/cursos
     */
    @GetMapping("/cursos")
    public ResponseEntity<List<Cursos>> getRankingCursos() {
        List<Cursos> ranking = cursosService.obtenerTop10Cursos();
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/top-curso")
    public ResponseEntity<?> getTopCurso() {
        Cursos topCurso = cursosService.obtenerCursoTop();
        if (topCurso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topCurso);
    }
}
