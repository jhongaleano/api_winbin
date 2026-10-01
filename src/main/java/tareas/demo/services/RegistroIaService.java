package tareas.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tareas.demo.models.*;
import tareas.demo.repository.*;

@Service
public class RegistroIaService {

    private final RegistroIaRepository registroIaRepository;
    private final DetalleSessionRepository sessionRepository;
    private final MaterialRepository materialRepository;
    private final CategoriaPuntajeRepository categoriaRepository;
    private final UsuarioService usuarioService;
    private final CursosService cursosService;

    public RegistroIaService(RegistroIaRepository registroIaRepository,
                        DetalleSessionRepository sessionRepository,
                        MaterialRepository materialRepository,
                        CategoriaPuntajeRepository categoriaRepository,
                        UsuarioService usuarioService,
                        CursosService cursosService) {
        this.registroIaRepository = registroIaRepository;
        this.sessionRepository = sessionRepository;
        this.materialRepository = materialRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioService = usuarioService;
        this.cursosService = cursosService;
    }

    @Transactional
    public RegistroIa guardarResultadoIA(RegistroIa nuevoRegistro) {
        DetalleSession sesion = sessionRepository.findById(nuevoRegistro.getIdSession())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Material mat = materialRepository.findById(nuevoRegistro.getIdMaterial())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        CategoriaPuntaje cat = categoriaRepository.findById(nuevoRegistro.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        nuevoRegistro.setSession(sesion);
        nuevoRegistro.setMaterial(mat);
        nuevoRegistro.setCategoria(cat);

        
        RegistroIa guardado = registroIaRepository.saveAndFlush(nuevoRegistro);
        usuarioService.notificarRankingsUsuariosWebsocket();
        cursosService.notificarRankingsCursosWebsocket();

        return guardado;
    }

    @Transactional
    public RegistroIa actualizarResultadoIA(Long id, RegistroIa cambios) {
        RegistroIa existente = registroIaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro IA no encontrado"));

        if(cambios.getConfianza() != null) existente.setConfianza(cambios.getConfianza());
        if(cambios.getUtlImagen() != null) existente.setUtlImagen(cambios.getUtlImagen());
        if(cambios.getIdSession() != null) existente.setIdSession(cambios.getIdSession());
        if(cambios.getMaterial() != null) existente.setMaterial(cambios.getMaterial());
        if(cambios.getCategoria() != null) existente.setCategoria(cambios.getCategoria());

        RegistroIa actualizado = registroIaRepository.saveAndFlush(existente);

        usuarioService.notificarRankingsUsuariosWebsocket();
        cursosService.notificarRankingsCursosWebsocket();

        return actualizado;
    }
}