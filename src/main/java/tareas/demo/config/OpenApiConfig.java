package tareas.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${server.url:" + OpenApiConstants.PRODUCTION_URL + "}")
    private String serverUrl;

    @Bean
    public OpenAPI winBinOpenAPI() {
        final String securitySchemeName = OpenApiConstants.BEARER_AUTH;

        return new OpenAPI()
                .info(new Info()
                        .title("WinBin API")
                        .description("""
                                # WinBin — API REST de reciclaje gamificado

                                Plataforma educativa para clasificar materiales reciclables con **IA**, \
                                acumular puntos, competir en rankings por usuario/curso y gestionar periodos de competencia.

                                ---

                                ## URLs de producción (Render)

                                | Recurso | URL |
                                |---------|-----|
                                | **Índice de endpoints** | [https://api-winbin.onrender.com/api](https://api-winbin.onrender.com/api) |
                                | **Swagger UI (documentación interactiva)** | [https://api-winbin.onrender.com/swagger-ui/index.html](https://api-winbin.onrender.com/swagger-ui/index.html) |
                                | **OpenAPI JSON** | [https://api-winbin.onrender.com/v3/api-docs](https://api-winbin.onrender.com/v3/api-docs) |
                                | **OpenAPI YAML** | [https://api-winbin.onrender.com/v3/api-docs.yaml](https://api-winbin.onrender.com/v3/api-docs.yaml) |
                                | **WebSocket STOMP** | `wss://api-winbin.onrender.com/ws` |

                                ---

                                ## Autenticación JWT

                                1. Ejecuta **`POST /api/auth/login`** con `documento` y `contrasenna`.
                                2. Copia el `token` de la respuesta.
                                3. En Swagger UI, pulsa **Authorize** e ingresa: `Bearer <token>`.
                                4. El token expira en **24 horas** (configurable con `jwt.expiration`).

                                **Header en peticiones manuales:**
                                ```
                                Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
                                ```

                                ---

                                ## Matriz de permisos

                                | Operación | Acceso |
                                |-----------|--------|
                                | `POST /api/auth/login` | Público |
                                | `POST /api/usuarios/registro` | Público |
                                | `GET /api/cursos` | Público |
                                | `GET /api/ranking/top-usuario`, `/top-curso` | Público |
                                | `GET /api/**` (resto) | USER, ESTUDIANTE o ADMIN + JWT |
                                | `POST /api/registroia/**` | Cualquier usuario autenticado + JWT |
                                | `POST`, `PUT`, `PATCH`, `DELETE /api/**` | Solo ADMIN + JWT |

                                ---

                                ## Flujo de sesión con IA (múltiples capturas)

                                1. **Login** → guardar `token` en el cliente (Flutter: `flutter_secure_storage`).
                                2. **Iniciar sesión de clasificación** → `POST /api/DetalleSession` **una vez** → guardar `id_session` (UUID).
                                3. **Cada foto** → enviar a la IA con el **mismo** `idSession`.
                                4. **IA guarda resultado** → `POST /api/registroia/guardar-resultado` con JWT + `idSession`, `idMaterial`, `idCategoria`.
                                5. **Terminar** → borrar `id_session` del cliente; el JWT sigue válido.

                                > **Importante:** El JWT identifica al **usuario**. El `id_session` identifica la **ronda de clasificación**. No son intercambiables.

                                ---

                                ## Paginación

                                Endpoints paginados (`/api/registroia`, `/api/auditoria`):

                                ```
                                ?page=0&size=20&sort=fecha,desc
                                ```

                                ---

                                ## Códigos HTTP habituales

                                | Código | Significado |
                                |--------|-------------|
                                | 200 | Éxito |
                                | 400 | Datos inválidos o faltantes |
                                | 401 | No autenticado / credenciales incorrectas |
                                | 403 | Sin permisos (rol insuficiente) |
                                | 404 | Recurso no encontrado |
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("WinBin Team")
                                .url("https://api-winbin.onrender.com/api")
                                .email("soporte@winbin.example"))
                        .license(new License().name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addServersItem(new Server()
                        .url(OpenApiConstants.PRODUCTION_URL)
                        .description("Producción — Render (api-winbin.onrender.com)"))
                .addServersItem(new Server()
                        .url(OpenApiConstants.LOCAL_URL)
                        .description("Desarrollo local"))
                .addServersItem(new Server()
                        .url(serverUrl)
                        .description("Servidor detectado por configuración (RENDER_EXTERNAL_URL o server.url)"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        Token JWT obtenido en POST /api/auth/login.
                                        Formato en header: Bearer <token>
                                        """)));
    }
}
