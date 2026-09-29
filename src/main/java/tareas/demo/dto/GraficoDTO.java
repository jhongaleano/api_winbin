package tareas.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos para los gráficos del dashboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraficoDTO {

    @Schema(example = "2026-09-29", description = "Fecha")
    private String fecha;

    @Schema(example = "15", description = "Cantidad total de la métrica")
    private Long cantidad;
}
