package tareas.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tareas.demo.models.PeriodoRanking;
import tareas.demo.repository.PeriodoRankingRepository;

@RestController
@RequestMapping("/api/PeriodoRanking")
public class PeriodoRankingController {

    @Autowired
    private PeriodoRankingRepository repositorio;

    @GetMapping
    public List<PeriodoRanking> listar() {
        return repositorio.findAll();
    }
}
