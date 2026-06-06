package tareas.demo.models;

import jakarta.persistence.*;
import java.util.*;
import lombok.Data;

@Entity
@Data
@Table(name = "materiales")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_material;

    @Column(name = "nombre_material")
    private String nombreMaterial;

    @ManyToMany
    @JoinTable(
        name = "materialRecursos",
        joinColumns = @JoinColumn(name = "id_material"),
        inverseJoinColumns = @JoinColumn(name = "id_recurso")
    )
    private List<RecursosMultimedia> recursosMultimedia;
}
