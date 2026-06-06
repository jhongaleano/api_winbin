package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.HistorialGanadores;
import tareas.demo.repository.HistorialGanadoresRepository;

@RestController
@RequestMapping("/api/HistorialGanadores")
public class HistorialGanadoresController {

    @Autowired
    private HistorialGanadoresRepository repositorio;

    @GetMapping
    public List<HistorialGanadores> listar() {
        return repositorio.findAll();
    }
}
