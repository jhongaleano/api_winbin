package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.PeriodoRanking;

public interface PeriodoRankingRepository
    extends JpaRepository<PeriodoRanking, Long> {}
