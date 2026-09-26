package tareas.demo.payload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciales de inicio de sesión")
public class LoginRequest {

    @Schema(description = "Documento del usuario", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String documento;

    @Schema(description = "Contraseña del usuario", example = "miPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contrasenna;

    // Constructor vacío (Obligatorio para que Spring procese el JSON)
    public LoginRequest() {}

    // Constructor con campos
    public LoginRequest(String documento, String contrasenna) {
        this.documento = documento;
        this.contrasenna = contrasenna;
    }

    // Getters y Setters (Fundamentales para que tu controlador no dé error)
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }
}
