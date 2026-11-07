package tareas.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tareas.demo.dto.GraficoDTO;
import tareas.demo.models.RegistroIa;

@Repository
public interface RegistroIaRepository extends JpaRepository<RegistroIa, Long> {

    @Query(value = "SELECT DATE_FORMAT(s.fecha_hora, '%Y-%m-%d') AS fecha, COUNT(r.id_ia) AS cantidad " +
            "FROM registroIA r " +
            "JOIN detalleSession s ON r.id_session = s.id_session " +
            "GROUP BY DATE_FORMAT(s.fecha_hora, '%Y-%m-%d') " +
            "ORDER BY DATE_FORMAT(s.fecha_hora, '%Y-%m-%d') ASC", nativeQuery = true)
    List<GraficoDTO> obtenerReciclajePorDia();

}
