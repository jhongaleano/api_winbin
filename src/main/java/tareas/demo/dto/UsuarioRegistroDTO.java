package tareas.demo.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Solicitud de registro de usuario")


@Data
public class UsuarioRegistroDTO {

    @Schema(example = "12345678", description = "Documento del usuario")
    private String documento;

    @Schema(example = "Juan", description = "Nombre del usuario")
    private String nombre;

    @Schema(example = "12345678", description = "Contraseña del usuario")
    private String contrasenna;

    @Schema(example = "1", description = "ID del curso")
    private Integer id_curso; 
}
