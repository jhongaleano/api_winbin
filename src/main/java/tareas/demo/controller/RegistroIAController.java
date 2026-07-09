package tareas.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.models.DetalleSession;
import tareas.demo.models.Material;
import tareas.demo.models.RegistroIa;
import tareas.demo.repository.RegistroIaRepository;
import org.springframework.http.ResponseEntity;
import tareas.demo.repository.DetalleSessionRepository;
import tareas.demo.repository.MaterialRepository;
import tareas.demo.repository.CategoriaPuntajeRepository;

@RestController
@RequestMapping("/api/registroia")

public class RegistroIAController {
    @Autowired
    private RegistroIaRepository repositorio;

    @Autowired private  DetalleSessionRepository sessionRepository; 

    @Autowired private  MaterialRepository materialRepository;

    @Autowired private  CategoriaPuntajeRepository categoriaRepository;

    
    public RegistroIAController(RegistroIaRepository repository, DetalleSessionRepository  sessionRepository, MaterialRepository materialRepository, CategoriaPuntajeRepository categoriaRepository) {
        this.repositorio = repository;
        this.sessionRepository = sessionRepository;
        this.materialRepository = materialRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RegistroIa> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @PostMapping("/guardar-resultado")

    public ResponseEntity<?> guardarDesdePython(@RequestBody RegistroIa nuevoRegistro) {
        System.out.println("ID Session recibido: " + nuevoRegistro.getIdSession());
    if (nuevoRegistro.getIdSession() == null || nuevoRegistro.getIdMaterial() == null) {
        return ResponseEntity.badRequest().body("Faltan IDs requeridos");
    }
    DetalleSession sesion = sessionRepository.findById(nuevoRegistro.getIdSession())
        .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

    Material mat = materialRepository.findById(nuevoRegistro.getIdMaterial())
        .orElseThrow(() -> new RuntimeException("Material no encontrado"));

    CategoriaPuntaje cat = categoriaRepository.findById(nuevoRegistro.getIdCategoria())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    nuevoRegistro.setSession(sesion);
    nuevoRegistro.setMaterial(mat);
    nuevoRegistro.setCategoria(cat);


    return ResponseEntity.ok(repositorio.save(nuevoRegistro));
}
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroIa> actualizar(@PathVariable Long id, @RequestBody RegistroIa cambios) {
        return repositorio.findById(id).map(existente -> {
            existente.setConfianza(cambios.getConfianza());
            existente.setUtlImagen(cambios.getUtlImagen());
            existente.setIdSession(cambios.getIdSession());
            existente.setMaterial(cambios.getMaterial());
            RegistroIa actualizado = repositorio.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
