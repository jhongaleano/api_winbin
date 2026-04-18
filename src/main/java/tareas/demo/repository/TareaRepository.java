package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import tareas.demo.models.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    
}
