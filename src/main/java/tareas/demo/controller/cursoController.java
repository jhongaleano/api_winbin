package tareas.demo.controller;
import tareas.demo.models.Cursos;
import tareas.demo.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")

public class cursoController{
    @Autowired
    private CursoRepository repositorio;

    @GetMapping
    public List<Cursos> listar(){
        return repositorio.findAll();
    }
    
}