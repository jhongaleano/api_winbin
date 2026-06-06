package tareas.demo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "detalleSession")
public class DetalleSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_session;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "documento")
    private usuarios documento;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaPuntaje id_categoria;

    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private PeriodoRanking id_periodo;
}
