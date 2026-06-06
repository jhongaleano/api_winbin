package tareas.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.RegistroIa;
import tareas.demo.repository.RegistroIaRepository;


@RestController
@RequestMapping("/api/registroia")

public class RegistroIAController {
    @Autowired
    private RegistroIaRepository repositorio;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RegistroIa> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @PostMapping("/guardar-resultado")
    public RegistroIa guardarDesdePython(@RequestBody RegistroIa nuevoRegistro) {
        return repositorio.save(nuevoRegistro);
    }

}
