package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.RecursosMultimedia;
import tareas.demo.repository.RecursosMultimediaRepository;

@RestController
@RequestMapping("/api/RecursosMultimedia")
public class RecursosMultimediaController {

    @Autowired
    private RecursosMultimediaRepository repositorio;

    @GetMapping
    public List<RecursosMultimedia> listar() {
        return repositorio.findAll();
    }
}
