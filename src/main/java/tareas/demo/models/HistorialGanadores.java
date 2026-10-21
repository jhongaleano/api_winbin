package tareas.demo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "historialGanadores")
public class HistorialGanadores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_historial;

    @Column(name = "puesto")
    private Integer puesto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_premio")
    private tipoPremio tipoPremio;

    @Column(name = "puntosLogrados")
    private Integer puntosLogrados;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "premio_dado")
    private String premioDado;

    @ManyToOne
    @JoinColumn(name = "documento")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private usuarios documento;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cursos id_curso;

    @ManyToOne
    @JoinColumn(name = "id_periodo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PeriodoRanking id_periodo;

    public enum tipoPremio {
        INDIVIDUAL,
        CURSO
    }
}
