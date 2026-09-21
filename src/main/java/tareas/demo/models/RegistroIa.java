package tareas.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import tareas.demo.config.AuditoriaListener;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@EntityListeners(AuditoriaListener.class)
@Data
@Schema(description = "Resultado de clasificación de material por IA")
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


    @Schema(description = "UUID de la sesión de clasificación (DetalleSession)", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    @Transient private UUID idSession;

    @Schema(description = "ID del material detectado", example = "1")
    @Transient private Integer idMaterial;

    @Schema(description = "ID de la categoría de puntaje", example = "2")
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
