package tareas.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "materiales")
public class Material{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_material;

    @Column(name = "nombre_material")
    private String nombreMaterial;
    
}



