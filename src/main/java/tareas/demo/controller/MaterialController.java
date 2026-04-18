package tareas.demo.controller;

import tareas.demo.models.Material;
import tareas.demo.repository.winbin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiales")

public class MaterialController {

    @Autowired
    private winbin repositorio;

    @GetMapping
    public List<Material> listar(){
        return repositorio.findAll();
    }
    
}
