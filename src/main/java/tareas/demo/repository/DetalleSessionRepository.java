package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.DetalleSession;

public interface DetalleSessionRepository
    extends JpaRepository<DetalleSession, Integer> {}
