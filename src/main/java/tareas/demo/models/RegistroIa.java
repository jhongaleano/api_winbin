package tareas.demo.models;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "confianza", precision = 10, scale = 0)
    private Double confianza;

    @Column(name = "utl_imagen", length = 100)
    private String utlImagen;

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false)
    private DetalleSession IdSession;

    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    private Material material;

}
