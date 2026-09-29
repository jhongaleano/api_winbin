package tareas.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tareas.demo.dto.GraficoDTO;
import tareas.demo.models.RegistroIa;
@Repository
public interface RegistroIaRepository extends JpaRepository<RegistroIa, Long> {

    @Query("SELECT new tareas.demo.dto.GraficoDTO(CAST(FUNCTION('DATE', r.session.fechaHora) AS string), COUNT(r)) " +
           "FROM RegistroIa r GROUP BY FUNCTION('DATE', r.session.fechaHora) ORDER BY FUNCTION('DATE', r.session.fechaHora) ASC")
    List<GraficoDTO> obtenerReciclajePorDia();
}
