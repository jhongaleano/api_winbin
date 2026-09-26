package tareas.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Solicitud de cambio de contraseña del usuario autenticado")
public class CambiarPasswordDTO {

    @Schema(description = "Contraseña actual", example = "vieja123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String passwordActual;

    @Schema(description = "Nueva contraseña", example = "nueva456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nuevaPassword;
    
    public CambiarPasswordDTO(String passwordActual, String nuevaPassword) {
        this.passwordActual = passwordActual;
        this.nuevaPassword = nuevaPassword;
    }

    public CambiarPasswordDTO() {
    }

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }
}
