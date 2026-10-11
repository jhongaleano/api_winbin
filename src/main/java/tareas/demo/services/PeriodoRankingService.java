package tareas.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tareas.demo.models.PeriodoRanking;
import tareas.demo.repository.PeriodoRankingRepository;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class PeriodoRankingService {

    private final PeriodoRankingRepository periodoRepository;
    private final CierrePeriodoService cierrePeriodoService;

    public PeriodoRankingService(PeriodoRankingRepository periodoRepository, CierrePeriodoService cierrePeriodoService) {
        this.periodoRepository = periodoRepository;
        this.cierrePeriodoService = cierrePeriodoService;
    }

    @Transactional
    public Optional<PeriodoRanking> obtenerOActualizarPeriodoActivo(){

        LocalDate hoy = LocalDate.now();

        Optional<PeriodoRanking> periodoActivoOpt = periodoRepository.findByActivoTrue();

        if(periodoActivoOpt.isPresent()){
            
            PeriodoRanking periodo = periodoActivoOpt.get();

            if (hoy.isAfter(periodo.getFechaFin())) {
                cierrePeriodoService.ejecutarCierreYReiniciarPuntos(periodo);

                periodo.setActivo(false);
                periodoRepository.save(periodo);
            }else if(hoy.isBefore(periodo.getFechaInicio()) ){
                periodo.setActivo(false);
                periodoRepository.save(periodo);
            }
            else{
                return Optional.of(periodo);
            }
            Optional<PeriodoRanking> nuevoPeriodoValido = periodoRepository
                    .findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(hoy, hoy);

            if (nuevoPeriodoValido.isPresent()) {
                PeriodoRanking nuevoActive = nuevoPeriodoValido.get();
                nuevoActive.setActivo(true);
                return Optional.of(periodoRepository.save(nuevoActive));
            }

            return Optional.empty();
        }

        Optional<PeriodoRanking> periodoParaHoy = periodoRepository
                .findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(hoy, hoy);

        if (periodoParaHoy.isPresent()) {
            PeriodoRanking p = periodoParaHoy.get();
            p.setActivo(true);
            return Optional.of(periodoRepository.save(p));
        }

        return Optional.empty();
                        

        
            

        
        
    }

    

}
