package tareas.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tareas.demo.dto.DashboardResumenDTO;
import tareas.demo.dto.GraficoDTO;
import tareas.demo.repository.CursoRepository;
import tareas.demo.repository.DetalleSessionRepository;
import tareas.demo.repository.MaterialRepository;
import tareas.demo.repository.RegistroIaRepository;
import tareas.demo.repository.UsuarioRepository;

@Service
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private  final CursoRepository cursoRepository;
    private final MaterialRepository materialRepository;
    private final DetalleSessionRepository detalleSessionRepository;
    private final RegistroIaRepository registroIaRepository;

    public DashboardService(UsuarioRepository usuarioRepository, CursoRepository cursoRepository, MaterialRepository materialRepository, DetalleSessionRepository detalleSessionRepository, RegistroIaRepository registroIaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.materialRepository = materialRepository;
        this.detalleSessionRepository = detalleSessionRepository;
        this.registroIaRepository = registroIaRepository;
    }

    public DashboardResumenDTO obtenerResumenCompleto() {
        long totalUsuarios = usuarioRepository.count();
        long usuariosInactivos = usuarioRepository.countByActivoFalse();
        long totalCursos = cursoRepository.count();
        long totalMateriales = materialRepository.count();
        long puntosTotales = usuarioRepository.sumarPuntosTodosLosUsuarios();
        
        List<GraficoDTO> sesiones = detalleSessionRepository.obtenerSesionesPorDia();
        List<GraficoDTO> reciclajes = registroIaRepository.obtenerReciclajePorDia();

        return new DashboardResumenDTO(
            totalUsuarios, usuariosInactivos, totalCursos, totalMateriales, 
            puntosTotales, sesiones, reciclajes
        );
    }
}
