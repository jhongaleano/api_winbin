package tareas.demo.config;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tareas.demo.models.Auditoria;
import tareas.demo.models.usuarios;
import tareas.demo.repository.UsuarioRepository;


@Component
public class AuditoriaListener {

    private static ApplicationEventPublisher eventPublisher;


    @Autowired
    public void init(ApplicationEventPublisher publisher) {
        AuditoriaListener.eventPublisher = publisher;
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

        if (entity instanceof Auditoria) {
            return;
        }

        Auditoria aud = new Auditoria();
        aud.setAccion(accion);
        aud.setTablaAfectada(entity.getClass().getSimpleName().toLowerCase());
        aud.setFecha(LocalDateTime.now());
        aud.setValorActual(entity.toString());

        String documentoUsuario = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                documentoUsuario = ((UserDetails) principal).getUsername();
            } else {
                documentoUsuario = principal.toString();
            }
        }

    if (documentoUsuario != null) {
            usuarios usuarioAsociado = new usuarios();
            usuarioAsociado.setDocumento(documentoUsuario); 
            aud.setDocumento(usuarioAsociado);
    } else {
            aud.setDocumento(null); 
    }

    if (eventPublisher != null) {
            eventPublisher.publishEvent(new AuditoriaEvent(aud, documentoUsuario));
        }
    }
}
