package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;


import tareas.demo.models.Material;


public interface MaterialRepository  extends JpaRepository<Material, Integer>{
    
}
