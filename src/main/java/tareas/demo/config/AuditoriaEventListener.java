package tareas.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tareas.demo.models.Auditoria;
import tareas.demo.models.usuarios;
import tareas.demo.services.AuditoriaService;

@Component
public class AuditoriaEventListener {

    @Autowired
    private AuditoriaService auditoriaService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleAuditoriaEvent(AuditoriaEvent event) {
        Auditoria aud = event.auditoria();
        
        if (event.documentoUsuario() != null) {
            usuarios usuarioAsociado = new usuarios();
            usuarioAsociado.setDocumento(event.documentoUsuario());
            aud.setDocumento(usuarioAsociado);
        }

        auditoriaService.guardarAuditoria(aud);
    }
}