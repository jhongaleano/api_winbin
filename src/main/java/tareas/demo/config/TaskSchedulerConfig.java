package tareas.demo.config;

import java.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import tareas.demo.models.PeriodoRanking;
import tareas.demo.repository.PeriodoRankingRepository;
import tareas.demo.services.CierrePeriodoService;

@Component
public class TaskSchedulerConfig {

    
    private final PeriodoRankingRepository periodoRepository;
    private final CierrePeriodoService cierrePeriodoService;

    public TaskSchedulerConfig(PeriodoRankingRepository periodoRepository, CierrePeriodoService cierrePeriodoService){
        this.periodoRepository = periodoRepository;
        this.cierrePeriodoService = cierrePeriodoService;
    }
    
    @Scheduled(cron = "0 0 0 * * ?")
    public void verificarYProcesarFinDePeriodo() {
        PeriodoRanking periodoActivo = periodoRepository.findByActivoTrue().orElse(null);

        if (periodoActivo != null && LocalDate.now().isAfter(periodoActivo.getFechaFin())) {
            
            cierrePeriodoService.ejecutarCierreYReiniciarPuntos(periodoActivo);

            periodoActivo.setActivo(false);
            periodoRepository.save(periodoActivo);
        }
    }

}
