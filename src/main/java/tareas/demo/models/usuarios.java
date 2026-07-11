package tareas.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import tareas.demo.config.AuditoriaListener;
@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "usuarios")

@SQLDelete(sql = "UPDATE usuarios SET activo = false WHERE documento = ?")
@SQLRestriction("activo = true")
public class usuarios {
    @Id
    @Column(name = "documento")
    private String documento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "rol", nullable = false)
    private String rol = "ROLE_USER";

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = true)
    private Cursos curso;

    @Column(name = "contrasenna",nullable = true)
    private String contrasenna;

    @Column(name = "puntos", nullable = false)
    private Integer puntos = 0;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        if (this.puntos == null) {
            this.puntos = 0;
        }
        if (this.rol == null || this.rol.trim().isEmpty()) {
            this.rol = "ROLE_USER";
        }
        if (this.activo == null) {
            this.activo = true; 
        }
    }

}
