package tareas.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DetalleSessionDTO {
    private UUID id_session;
    private LocalDateTime fechaHora;
    private UsuarioRegistroDTO documento;
    private String id_periodo;
    private Integer id_categoria;
}
