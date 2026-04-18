package tareas.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tareas.demo.models.Material;

@RepositoryRestResource(path = "materiales")
public interface winbin extends JpaRepository<Material, Integer>{
    
}
