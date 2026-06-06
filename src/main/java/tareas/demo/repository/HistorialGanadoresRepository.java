package tareas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tareas.demo.models.HistorialGanadores;

public interface HistorialGanadoresRepository
    extends JpaRepository<HistorialGanadores, Integer> {}
