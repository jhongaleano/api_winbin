package tareas.demo;

import org.junit.jupiter.api.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WinBinIntegracionApiTest {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String API_URL = "https://winbin-api.duckdns.org";

    @Test
    @Order(1)
    @DisplayName("CP-05: POST /api/auth/login con credenciales válidas responde 200 OK con token")
    void testCp05LoginApi() throws Exception {
        String json = "{\"documento\":\"000000000\",\"clave_prueba\":\"\"}";

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, res.statusCode(), "Debe responder HTTP 200 OK");
        assertTrue(res.body().contains("token"), "La respuesta debe incluir el campo token");
        System.out.println("CP-05 OK | Status: " + res.statusCode() + " | Token recibido correctamente");
    }

    @Test
    @Order(2)
    @DisplayName("CP-06: GET /api/usuarios/perfil sin token responde HTTP 401/403")
    void testCp05bSinToken401() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/usuarios/perfil"))
                .GET()
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        assertTrue(res.statusCode() == 401 || res.statusCode() == 403,
                "Debe bloquear el acceso sin token con 401 o 403");
        System.out.println("CP-05B OK | Bloqueado sin token con Status: " + res.statusCode());
    }

    @Test
    @Order(3)
    @DisplayName("CP-07: GET /api/auditoria con rol ESTUDIANTE responde HTTP 403")
    void testCp06RolEstudiante403() throws Exception {
        String loginEst = "{\"documento\":\"00000000000\",\"contrasenna\":\"clave_prueba\"}";

        HttpResponse<String> resLogin = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + "/api/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(loginEst))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, resLogin.statusCode(), "El login del estudiante de prueba debe ser 200 OK");
        String tokenEstudiante = resLogin.body().replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");

        HttpRequest reqAuditoria = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/arduino/pendiente"))
                .header("Authorization", "Bearer " + tokenEstudiante)
                .GET()
                .build();

        HttpResponse<String> res = client.send(reqAuditoria, HttpResponse.BodyHandlers.ofString());

        assertEquals(403, res.statusCode(), "Un estudiante no debe acceder a auditoría (403 Forbidden)");
        System.out.println("CP-06 OK | Acceso denegado a estudiante con Status: " + res.statusCode());
    }
}