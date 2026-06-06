package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.Auditoria;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    
}
