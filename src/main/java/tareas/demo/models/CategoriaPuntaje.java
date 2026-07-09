package tareas.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "categoriaPuntaje")


public class CategoriaPuntaje{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamanno")
    private Tamano tamanno;

    @Column(name = "puntos")
    private Integer puntos;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Material id_material;
}

enum Tamano {
    pequeño,
    mediano,
    grande,
}
