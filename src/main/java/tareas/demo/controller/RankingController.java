package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.Cursos;
import tareas.demo.models.usuarios;
import tareas.demo.services.*;

@RestController
@RequestMapping("/api/ranking") // Permite que Flutter se conecte sin problemas de CORS
public class RankingController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursosService cursosService;

    /**
     * Endpoint para obtener el Ranking General de Usuarios
     * URL: GET http://localhost:8080/api/ranking/general
     */
    @GetMapping("/general")
    public ResponseEntity<List<usuarios>> getRankingGeneral() {
        List<usuarios> ranking = usuarioService.obtenerTop10Estudiantes();
        return ResponseEntity.ok(ranking);
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
}
