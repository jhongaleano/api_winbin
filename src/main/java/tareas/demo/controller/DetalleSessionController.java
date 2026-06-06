package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.DetalleSession;
import tareas.demo.repository.DetalleSessionRepository;

@RestController
@RequestMapping("/api/DetalleSession")
public class DetalleSessionController {

    @Autowired
    private DetalleSessionRepository repositorio;

    @GetMapping
    public List<DetalleSession> listar() {
        return repositorio.findAll();
    }
}
