package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tareas.demo.dto.GraficoDTO;
import tareas.demo.models.DetalleSession;
import java.util.List;
import java.util.UUID;

@Repository
public interface DetalleSessionRepository
        extends JpaRepository<DetalleSession, UUID> {
    @Query(value = "SELECT DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') AS fecha, COUNT(d.id_session) AS cantidad " +
            "FROM detalle_session d " +
            "GROUP BY DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') " +
            "ORDER BY DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') ASC", nativeQuery = true)
    List<GraficoDTO> obtenerSesionesPorDia();

}
