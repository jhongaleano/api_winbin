package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.DetalleSession;
import java.util.UUID;

public interface DetalleSessionRepository
    extends JpaRepository<DetalleSession, UUID> {}
