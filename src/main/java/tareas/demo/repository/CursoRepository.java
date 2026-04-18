package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import tareas.demo.models.Cursos;


public interface CursoRepository extends JpaRepository<Cursos, Integer>{
    
}

