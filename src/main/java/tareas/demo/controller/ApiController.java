package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tareas.demo.config.OpenApiConstants;

@RestController
@RequestMapping("/api")
@Tag(name = "Utilidades", description = "Índice automático de endpoints disponibles")
public class ApiController {

    private final RequestMappingHandlerMapping handlerMapping;

    @Value("${server.url:" + OpenApiConstants.PRODUCTION_URL + "}")
    private String configuredServerUrl;

    public ApiController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Operation(
            summary = "Índice de endpoints",
            description = """
                    Devuelve un mapa JSON con todos los endpoints REST registrados bajo `/api`.

                    **Producción:** https://api-winbin.onrender.com/api

                    **Documentación interactiva:** https://api-winbin.onrender.com/swagger-ui/index.html
                    """
    )
    @GetMapping
    public Map<String, String> indexarEndpoints(HttpServletRequest request) {
        Map<String, String> endpoints = new TreeMap<>();
        String baseUrl = resolveBaseUrl(request);

        handlerMapping.getHandlerMethods().forEach((key, value) -> {
            key.getDirectPaths().forEach(path -> {
                if (path.startsWith("/api") && !path.equals("/api")) {
                    String nombreClave = generarNombreClave(path);
                    endpoints.put(nombreClave, baseUrl + path);
                }
            });
        });

        return endpoints;
    }

    /**
     * Resuelve la URL base sin usar String.replace("/api"), que rompe hostnames
     * como https://api-winbin.onrender.com (elimina el "api" del dominio).
     */
    private String resolveBaseUrl(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        String uri = request.getRequestURI();

        if (uri != null && !uri.isEmpty() && requestUrl.endsWith(uri)) {
            return requestUrl.substring(0, requestUrl.length() - uri.length());
        }

        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        String forwardedHost = request.getHeader("X-Forwarded-Host");
        if (forwardedProto != null && forwardedHost != null) {
            return forwardedProto + "://" + forwardedHost;
        }

        return configuredServerUrl.replaceAll("/$", "");
    }

    private String generarNombreClave(String path) {
        String limpio = path.replace("/api/", "").replaceAll("/\\{.*\\}", "");
        if (limpio.contains("-")) {
            String[] partes = limpio.split("-");
            StringBuilder sb = new StringBuilder(partes[0]);
            for (int i = 1; i < partes.length; i++) {
                sb.append(partes[i].substring(0, 1).toUpperCase()).append(
                    partes[i].substring(1)
                );
            }
            return sb.toString();
        }
        return limpio.isEmpty() ? "raiz" : limpio;
    }
}
