package tareas.demo.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "periodoRanking")
public class PeriodoRanking {

    @Id
    @Column(name = "id_periodo",length = 30)
    private String id_periodo;

    @Column(name = "nombre_periodo")
    private String nombrePeriodo;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo")
    private Boolean activo;

    @PrePersist
    public void generarIdUnico() {
        if (this.id_periodo == null || this.id_periodo.trim().isEmpty()) {
            LocalDate fechaReferencia = (this.fechaInicio != null) ? this.fechaInicio : LocalDate.now();
            int anio = fechaReferencia.getYear();
            String mes = String.format("%02d", fechaReferencia.getMonthValue());
            int aleatorio = (int) (Math.random() * 900000) + 100000;
            this.id_periodo = "PER-" + anio + "-" + mes + "-" + aleatorio;
        }
    }
}
