package tareas.demo.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import tareas.demo.config.AuditoriaListener;
@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "usuarios")

public class usuarios {
    @Id
    @Column(name = "documento")
    private String documento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "rol")
    private String rol;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = true)
    private Cursos curso;

    @Column(name = "contrasenna",nullable = true)
    private String contrasenna;

    @Column(name = "puntos")
    private Integer puntos;

}
