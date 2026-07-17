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

    public PeriodoRankingService(PeriodoRankingRepository periodoRepository) {
        this.periodoRepository = periodoRepository;
    }

    @Transactional
    public Optional<PeriodoRanking> obtenerOActualizarPeriodoActivo(){

        LocalDate hoy = LocalDate.now();

        Optional<PeriodoRanking> periodoActivoOpt = periodoRepository.findByActivoTrue();

        if(periodoActivoOpt.isPresent()){
            
            PeriodoRanking periodo = periodoActivoOpt.get();

            if(hoy.isBefore(periodo.getFechaInicio()) || hoy.isAfter(periodo.getFechaFin())){
                periodo.setActivo(false);
                periodoRepository.save(periodo);

                Optional<PeriodoRanking> nuevoPeriodoValido = periodoRepository
                        .findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(hoy, hoy);

                if (nuevoPeriodoValido.isPresent()) {
                    PeriodoRanking nuevoActive = nuevoPeriodoValido.get();
                    nuevoActive.setActivo(true);
                    return Optional.of(periodoRepository.save(nuevoActive));
                }

                return Optional.empty(); 
            }
            return Optional.of(periodo);
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
