package tareas.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tareas.demo.models.usuarios;



import java.util.List;
import java.util.Optional;



public interface UsuarioRepository extends JpaRepository<usuarios, String> {
    
    Optional<usuarios> findByDocumento(String documento);

    List<usuarios> findTop10ByOrderByPuntosDesc(Pageable pageable);

    @Query(value = "SELECT * FROM usuarios WHERE documento = :documento", nativeQuery = true)
    Optional<usuarios> findByDocumentoIncluyendoInactivos(@Param("documento") String documento);

    @Query(value = "SELECT * FROM usuarios WHERE activo = false", nativeQuery = true)
    List<usuarios> findAllInactivos();

    @Query(value = "SELECT * FROM usuarios", nativeQuery = true)
    List<usuarios> findAllIncluyendoInactivos();

    void deleteById(String documento);
}
