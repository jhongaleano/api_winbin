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

    public TaskSchedulerConfig(PeriodoRankingRepository periodoRepository, CierrePeriodoService cierrePeriodoService) {
        this.periodoRepository = periodoRepository;
        this.cierrePeriodoService = cierrePeriodoService;
    }

    @Scheduled(cron = "0 0 0 * * ?" , zone = "America/Bogota")
    public void verificarYProcesarFinDePeriodo() {
        PeriodoRanking periodoActivo = periodoRepository.findByActivoTrue().orElse(null);

        if (periodoActivo != null && LocalDate.now().isAfter(periodoActivo.getFechaFin())) {
            try {
                System.out.println("Iniciando cierre de periodo...");

                cierrePeriodoService.ejecutarCierreYReiniciarPuntos(periodoActivo);

                periodoActivo.setActivo(false);
                periodoRepository.save(periodoActivo);

                System.out.println("Periodo cerrado exitosamente.");

            } catch (Exception e) {
                System.err.println("Error al cerrar el periodo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
