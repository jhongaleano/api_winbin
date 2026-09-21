package tareas.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Column(name = "documento", nullable = false)
    private String documento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol = Rol.ESTUDIANTE;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Cursos curso;

    @Column(name = "contrasenna",nullable = false)
    private String contrasenna;

    @Column(name = "puntos", nullable = false)
    private Integer puntos = 0;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;

    public enum Rol {
        ESTUDIANTE,
        ADMIN
    }

    @PrePersist
    public void prePersist() {
        if (this.puntos == null) {
            this.puntos = 0;
        }
        if (this.rol == null) {
            this.rol = Rol.ESTUDIANTE;
        }
        if (this.activo == null) {
            this.activo = true; 
        }


     

    }

}
