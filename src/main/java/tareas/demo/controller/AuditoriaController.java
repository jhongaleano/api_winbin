package tareas.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.Auditoria;
import tareas.demo.repository.AuditoriaRepository;

@RestController
@RequestMapping("/api/auditoria")

public class AuditoriaController {
    private final AuditoriaRepository repositorio;

    public AuditoriaController(AuditoriaRepository repositorio){
        this.repositorio = repositorio;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Auditoria> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

}
