package tareas.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.RegistroIa;

public interface RegistroIaRepository extends JpaRepository<RegistroIa, Long> {
    Optional<RegistroIa> findById(Long id);
}
