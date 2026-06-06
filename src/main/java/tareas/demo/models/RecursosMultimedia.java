package tareas.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.*;
import lombok.Data;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "recursosMultimedia")
public class RecursosMultimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_recurso;

    @Column(name = "tipo_recurso")
    private String tipoRecurso;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "url_archivo")
    private String urlArchivo;

    @Column(name = "estado")
    private String estado;

    @ManyToMany(mappedBy = "recursosMultimedia")
    @JsonIgnoreProperties("recursosMultimedia")
    private List<Material> material;
}
