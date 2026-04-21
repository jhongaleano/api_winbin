package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tareas.demo.models.usuarios;
import java.util.*;


public interface UsuarioRepository extends JpaRepository<usuarios, Integer> {
    Optional<usuarios> findByDocumento(Integer documento);
}
