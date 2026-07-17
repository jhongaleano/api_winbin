package tareas.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import tareas.demo.config.AuditoriaListener;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Table(name = "registroIA")

public class RegistroIa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ia")
    private Integer idIa ;

    @Column(name = "confianza")
    private Double confianza;

    @Column(name = "utl_imagen", length = 100)
    private String utlImagen;


    @Transient private UUID idSession;
    @Transient private Integer idMaterial;
    @Transient private Integer idCategoria;


    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false)
    private DetalleSession session; 

    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaPuntaje categoria;


}
