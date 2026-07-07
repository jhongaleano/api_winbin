package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // 👈 Importante
import org.springframework.transaction.annotation.Transactional;
import tareas.demo.models.usuarios;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface UsuarioRepository extends JpaRepository<usuarios, String> {
    Optional<usuarios> findByDocumento(String documento);

    List<usuarios> findTop10ByOrderByPuntosDesc(Pageable pageable);

    @Modifying
    @Transactional
    void deleteByDocumento(String documento);
}
