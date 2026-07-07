package tareas.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tareas.demo.models.Auditoria;
import tareas.demo.repository.AuditoriaRepository;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    // REQUIRES_NEW abre una transacción independiente y evita el conflicto de llaves
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardarAuditoria(Auditoria aud) {
        auditoriaRepository.save(aud);
    }
}