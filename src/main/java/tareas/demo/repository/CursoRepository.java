package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import tareas.demo.models.Cursos;
import java.util.List;


public interface CursoRepository extends JpaRepository<Cursos, Integer>{
    List<Cursos> findByOrderByPuntosTotalesDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE Cursos c SET c.puntosTotales = 0")
    void reiniciarPuntosCursos();
}

