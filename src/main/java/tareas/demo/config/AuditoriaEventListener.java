package tareas.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tareas.demo.models.Auditoria;
import tareas.demo.models.usuarios;
import tareas.demo.services.AuditoriaService;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

@Component
public class AuditoriaEventListener {

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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

        String mensaje = construirMensaje(aud);
        Map<String, Object> payload = Map.of(
                "accion", aud.getAccion(),
                "tabla", aud.getTablaAfectada(),
                "usuario", event.documentoUsuario() != null ? event.documentoUsuario() : "sistema",
                "mensaje", mensaje
        );

        messagingTemplate.convertAndSend("/topic/auditoria", payload, (MessageHeaders) null);
    }

    private String construirMensaje(Auditoria aud) {
        String accion = switch (aud.getAccion()) {
            case "INSERT" -> "creó un registro en";
            case "UPDATE" -> "actualizó un registro en";
            case "DELETE" -> "eliminó un registro de";
            default -> "realizó una acción en";
        };
        String usuario = aud.getDocumento() != null
                ? aud.getDocumento().getDocumento()
                : "Sistema";
        return usuario + " " + accion + " " + aud.getTablaAfectada();
    }
}