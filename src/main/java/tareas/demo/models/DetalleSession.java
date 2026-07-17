package tareas.demo.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "detalleSession")
public class DetalleSession {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id_session", length = 36)
    private UUID id_session;

    @Column(name = "fecha_hora",nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "documento",nullable = false)
    private usuarios documento;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_categoria", nullable = true)
    private CategoriaPuntaje id_categoria;

    @ManyToOne
    @JoinColumn(name = "id_periodo",nullable = false)
    private PeriodoRanking id_periodo;

    @PrePersist
    public void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now(); 
        }
    }
}
