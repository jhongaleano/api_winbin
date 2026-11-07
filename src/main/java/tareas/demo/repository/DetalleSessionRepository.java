package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tareas.demo.dto.GraficoDTO;
import tareas.demo.models.DetalleSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DetalleSessionRepository
                extends JpaRepository<DetalleSession, UUID> {
        @Query(value = "SELECT DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') AS fecha, COUNT(d.id_session) AS cantidad " +
                        "FROM detalleSession d " +
                        "GROUP BY DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') " +
                        "ORDER BY DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') ASC", nativeQuery = true)
        List<GraficoDTO> obtenerSesionesPorDia();

        @Query("SELECT d FROM DetalleSession d WHERE " +
                        "(:documento IS NULL OR d.documento.documento = :documento) AND " +
                        "(:idPeriodo IS NULL OR d.id_periodo.id_periodo = :idPeriodo) AND " +
                        "(:inicioDia IS NULL OR d.fechaHora >= :inicioDia) AND " +
                        "(:finDia IS NULL OR d.fechaHora <= :finDia)")
        List<DetalleSession> buscarPorFiltros(
                        @Param("documento") String documento,
                        @Param("idPeriodo") String idPeriodo,
                        @Param("inicioDia") LocalDateTime inicioDia,
                        @Param("finDia") LocalDateTime finDia);

}
