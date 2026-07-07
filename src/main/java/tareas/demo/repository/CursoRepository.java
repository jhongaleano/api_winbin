package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.*;
import tareas.demo.models.Cursos;


public interface CursoRepository extends JpaRepository<Cursos, Integer>{
    List<Cursos> findByOrderByPuntosTotalesDesc(Pageable pageable);
}

