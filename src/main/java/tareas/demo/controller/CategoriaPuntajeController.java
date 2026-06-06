package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.repository.CategoriaPuntajeRepository;

@RestController
@RequestMapping("/api/CategoriaPuntaje")
public class CategoriaPuntajeController {

    @Autowired
    private CategoriaPuntajeRepository repositorio;

    @GetMapping
    public List<CategoriaPuntaje> listar() {
        return repositorio.findAll();
    }
}
