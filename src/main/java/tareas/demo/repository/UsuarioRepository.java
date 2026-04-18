package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tareas.demo.models.usuarios;

public interface UsuarioRepository extends JpaRepository<usuarios, Integer> {
    
}
