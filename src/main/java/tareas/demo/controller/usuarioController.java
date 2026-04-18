package tareas.demo.controller;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class usuarioController {
    @Autowired
    private UsuarioRepository repositorio;

    @GetMapping
    public List<usuarios> listar(){
        return repositorio.findAll();
    }
}
