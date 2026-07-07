package tareas.demo.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tareas.demo.models.Cursos;
import tareas.demo.repository.CursoRepository;

/**
 * CursosService
 */
@Service
public class CursosService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Cursos> obtenerTop10Cursos() {
        // PageRequest.of(numeroDePagina, tamannoDePagina)
        // La página 0 es la primera página. El 10 es la cantidad de filas que quieres.
        Pageable topDiez = PageRequest.of(0, 10);

        return cursoRepository.findByOrderByPuntosTotalesDesc(topDiez);
    }

    public List<Cursos> obtenerSiguientes10Cursos() {
        // Si en la app móvil el usuario desliza hacia abajo (scroll), pides la página 1
        Pageable siguientesDiez = PageRequest.of(1, 10);

        return cursoRepository.findByOrderByPuntosTotalesDesc(siguientesDiez);
    }
}
