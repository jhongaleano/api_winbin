package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.RecursosMultimedia;

public interface RecursosMultimediaRepository
    extends JpaRepository<RecursosMultimedia, Integer> {}
