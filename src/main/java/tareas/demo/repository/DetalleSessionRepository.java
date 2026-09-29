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
        @Query("SELECT new tareas.demo.dto.GraficoDTO(CAST(FUNCTION('DATE', d.fechaHora) AS string), COUNT(d)) " +
           "FROM DetalleSession d GROUP BY FUNCTION('DATE', d.fechaHora) ORDER BY FUNCTION('DATE', d.fechaHora) ASC")
    List<GraficoDTO> obtenerSesionesPorDia();

    }
