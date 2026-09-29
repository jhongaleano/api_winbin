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
    @Query("SELECT new tareas.demo.dto.GraficoDTO(FUNCTION('DATE_FORMAT', d.fechaHora, '%Y-%m-%d'), COUNT(d)) " +
            "FROM DetalleSession d " +
            "GROUP BY FUNCTION('DATE_FORMAT', d.fechaHora, '%Y-%m-%d') " +
            "ORDER BY FUNCTION('DATE_FORMAT', d.fechaHora, '%Y-%m-%d') ASC")
    List<GraficoDTO> obtenerSesionesPorDia();

}
