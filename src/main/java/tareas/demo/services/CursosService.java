package tareas.demo.services;

import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tareas.demo.models.Cursos;
import tareas.demo.repository.CursoRepository;

/**
 * CursosService
 */
@Service
public class CursosService {

    private final CursoRepository cursoRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public CursosService(CursoRepository cursoRepository, SimpMessagingTemplate messagingTemplate){
        this.cursoRepository = cursoRepository;
        this.messagingTemplate = messagingTemplate;
    }
    
    public List<Cursos> obtenerTop10Cursos() {
        // PageRequest.of(numeroDePagina, tamannoDePagina)
        // La página 0 es la primera página. El 10 es la cantidad de filas que quieres.
        Pageable topDiez = PageRequest.of(0, 10);

        return cursoRepository.findByOrderByPuntosTotalesDesc(topDiez);
    }

    public Cursos obtenerCursoTop() {

        Pageable top = PageRequest.of(0, 1);
        List<Cursos> resultado = cursoRepository.findByOrderByPuntosTotalesDesc(top);
        return resultado.isEmpty() ? null : resultado.get(0);
    } 

    public List<Cursos> obtenerCursosPaginados(int numeroPagina) {
        Pageable pagina = PageRequest.of(numeroPagina, 10);
        return cursoRepository.findByOrderByPuntosTotalesDesc(pagina);
    }
    public void notificarRankingsCursosWebsocket() {
        messagingTemplate.convertAndSend("/topic/ranking/cursos-top10", obtenerTop10Cursos());
        messagingTemplate.convertAndSend("/topic/ranking/curso-top", obtenerCursoTop());
    }
}
