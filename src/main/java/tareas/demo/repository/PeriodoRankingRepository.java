package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.PeriodoRanking;
import java.time.LocalDate;
import java.util.Optional;

public interface PeriodoRankingRepository extends JpaRepository<PeriodoRanking, String> {
     Optional<PeriodoRanking> findByActivoTrue();

   
    Optional<PeriodoRanking> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fecha1, LocalDate fecha2);



}
