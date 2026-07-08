package tareas.demo.payload;

public class LoginRequest {

    private String documento;
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
