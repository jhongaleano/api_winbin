package tareas.demo.config;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tareas.demo.models.Auditoria;
import tareas.demo.repository.AuditoriaRepository;
import java.time.LocalDateTime;

@Component
public class AuditoriaListener {

    private static AuditoriaRepository auditoriaRepository;

    @Autowired
    public void init(AuditoriaRepository repository) {
        AuditoriaListener.auditoriaRepository = repository;
    }

    @PostPersist
    public void trasInsertar(Object entity) {
        registrarAccion("INSERT", entity);
    }

    @PostUpdate
    public void trasActualizar(Object entity) {
        registrarAccion("UPDATE", entity);
    }

    @PostRemove
    public void trasEliminar(Object entity) {
        registrarAccion("DELETE", entity);
    }

    private void registrarAccion(String accion, Object entity) {
        Auditoria aud = new Auditoria();
        aud.setAccion(accion);
        aud.setTablaAfectada(entity.getClass().getSimpleName().toLowerCase());
        aud.setFecha(LocalDateTime.now());
        aud.setValorActual(entity.toString());
        
        auditoriaRepository.save(aud);
    }
}