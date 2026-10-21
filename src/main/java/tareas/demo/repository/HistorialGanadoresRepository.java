package tareas.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tareas.demo.models.HistorialGanadores;

public interface HistorialGanadoresRepository extends JpaRepository<HistorialGanadores, Integer> {

    
    @Query("SELECT h FROM HistorialGanadores h WHERE h.id_periodo.id_Periodo = :idPeriodo ORDER BY h.puesto ASC")
    List<HistorialGanadores> findByPeriodoIdOrderByPuestoAsc(@Param("idPeriodo") Integer idPeriodo);
}