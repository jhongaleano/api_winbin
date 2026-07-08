package tareas.demo.config;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tareas.demo.models.Auditoria;

@Component
public class AuditoriaListener {

    // Cambiar el atributo estático al servicio
    private static tareas.demo.services.AuditoriaService auditoriaService;

    @Autowired
    public void init(tareas.demo.services.AuditoriaService service) {
        AuditoriaListener.auditoriaService = service;
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

        // Usar el servicio con transacción independiente
        auditoriaService.guardarAuditoria(aud);
    }
}
