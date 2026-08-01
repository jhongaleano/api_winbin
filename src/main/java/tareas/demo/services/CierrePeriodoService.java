package tareas.demo.services;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tareas.demo.models.Cursos;
import tareas.demo.models.HistorialGanadores;
import tareas.demo.models.PeriodoRanking;
import tareas.demo.models.usuarios;
import tareas.demo.models.HistorialGanadores.tipoPremio;
import tareas.demo.repository.CursoRepository;
import tareas.demo.repository.HistorialGanadoresRepository;
import tareas.demo.repository.UsuarioRepository;

@Service
public class CierrePeriodoService {

    private final UsuarioRepository usuarioRepository;

    private final CursoRepository cursoRepository;
    private HistorialGanadoresRepository historialRepository;

    public CierrePeriodoService(UsuarioRepository usuarioRepository, CursoRepository cursoRepository, HistorialGanadoresRepository historialRepository){
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.historialRepository = historialRepository;
    }


    @Transactional
    public void ejecutarCierreYReiniciarPuntos(PeriodoRanking periodoActual) {
        Pageable topDiez = PageRequest.of(0, 10);
        List<HistorialGanadores> historialAGuardar = new ArrayList<>();

        List<usuarios> topUsuarios = usuarioRepository.findTop10ByOrderByPuntosDesc(topDiez);
        for (int i = 0; i < topUsuarios.size(); i++) {
            usuarios u = topUsuarios.get(i);

            HistorialGanadores h = new HistorialGanadores();
            h.setPuesto(i + 1); 
            h.setTipoPremio(tipoPremio.INDIVIDUAL);
            h.setPuntosLogrados(u.getPuntos()); 
            h.setFecha(LocalDate.now());
            h.setDocumento(u);
            h.setId_curso(u.getCurso());
            h.setId_periodo(periodoActual);
            h.setPremioDado("Por definir");

            historialAGuardar.add(h);
        }

        List<Cursos> topCursos = cursoRepository.findByOrderByPuntosTotalesDesc(topDiez);
        for (int i = 0; i < topCursos.size(); i++) {
            Cursos c = topCursos.get(i);

            HistorialGanadores h = new HistorialGanadores();
            h.setPuesto(i + 1);
            h.setTipoPremio(tipoPremio.CURSO);
            h.setPuntosLogrados(c.getPuntosTotales());
            h.setFecha(LocalDate.now());
            h.setDocumento(null);
            h.setId_curso(c);
            h.setId_periodo(periodoActual);
            h.setPremioDado("Por definir");

            historialAGuardar.add(h);
        }
        historialRepository.saveAll(historialAGuardar);

        usuarioRepository.reiniciarPuntosUsuarios();
        cursoRepository.reiniciarPuntosCursos();
    }
}
