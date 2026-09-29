package tareas.demo.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(description = "Resumen del dashboard")
@Data


public class DashboardResumenDTO {
    
    @Schema(example = "100", description = "Total de usuarios")
    private long totalUsuarios;

    @Schema(example = "10", description = "Total de usuarios inactivos")
    private long usuariosInactivos;

    @Schema(example = "10", description = "Total de cursos")
    private long totalCursos;

    @Schema(example = "10", description = "Total de materiales")
    private long totalMateriales;

    @Schema(example = "10", description = "Total de puntos")
    private long puntosTotalesUsuarios;
    
    @Schema(description = "Sesiones por día")
    private List<GraficoDTO> sesionesPorDia;
    
    @Schema(description = "Reciclaje por día")
    private List<GraficoDTO> reciclajePorDia;

    public DashboardResumenDTO(long totalUsuarios, long usuariosInactivos, long totalCursos, 
                               long totalMateriales, long puntosTotalesUsuarios, 
                               List<GraficoDTO> sesionesPorDia, List<GraficoDTO> reciclajePorDia) {
        this.totalUsuarios = totalUsuarios;
        this.usuariosInactivos = usuariosInactivos;
        this.totalCursos = totalCursos;
        this.totalMateriales = totalMateriales;
        this.puntosTotalesUsuarios = puntosTotalesUsuarios;
        this.sesionesPorDia = sesionesPorDia;
        this.reciclajePorDia = reciclajePorDia;
    }
}

