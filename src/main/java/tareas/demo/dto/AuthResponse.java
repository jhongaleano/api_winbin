package tareas.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta exitosa del login")
public class AuthResponse {

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Documento del usuario autenticado", example = "1234567890")
    private String documento;

    public AuthResponse() {
    }

    public AuthResponse(String token, String documento) {
        this.token = token;
        this.documento = documento;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
