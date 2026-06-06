package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.CategoriaPuntaje;

public interface CategoriaPuntajeRepository
    extends JpaRepository<CategoriaPuntaje, Integer> {}
