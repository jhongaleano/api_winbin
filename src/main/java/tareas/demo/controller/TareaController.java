package tareas.demo.controller; // Verifica que este sea tu paquete real

import tareas.demo.models.Tarea;
import tareas.demo.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaRepository repositorio;

    // 1. Obtener todas las tareas
    @GetMapping
    public List<Tarea> listar() {
        return repositorio.findAll();
    }

    // 2. Obtener una tarea por ID (Soluciona el 404 que tenías)
    @GetMapping("/{id}")
    public Tarea obtenerPorId(@PathVariable Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la tarea con ID: " + id));
    }

    // 3. Crear una tarea (POST)
    @PostMapping
    public Tarea crear(@RequestBody Tarea tarea) {
        return repositorio.save(tarea);
    }

    // 4. Actualizar una tarea (PUT)
    @PutMapping("/{id}")
    public Tarea actualizar(@PathVariable Long id, @RequestBody Tarea tareaActualizada) {
        return repositorio.findById(id)
                .map(tarea -> {
                    tarea.setDescripcion(tareaActualizada.getDescripcion());
                    tarea.setCompletada(tareaActualizada.isCompletada());
                    return repositorio.save(tarea);
                })
                .orElseThrow(() -> new RuntimeException("No se pudo actualizar, ID no existe"));
    }

    // 5. Eliminar una tarea (DELETE)
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repositorio.deleteById(id);
    }
}