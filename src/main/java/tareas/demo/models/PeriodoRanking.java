package tareas.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "periodoRanking")
public class PeriodoRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_periodo;

    @Column(name = "nombre_periodo")
    private String nombrePeriodo;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo")
    private Boolean activo;
}
