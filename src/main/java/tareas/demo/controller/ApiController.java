package tareas.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api") 
public class ApiController {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @GetMapping
    public Map<String, String> indexarEndpoints(HttpServletRequest request) {
        Map<String, String> endpoints = new TreeMap<>();
        
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

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

   
    private String generarNombreClave(String path) {
        String limpio = path.replace("/api/", "").replaceAll("/\\{.*\\}", "");
        if (limpio.contains("-")) {
            String[] partes = limpio.split("-");
            StringBuilder sb = new StringBuilder(partes[0]);
            for (int i = 1; i < partes.length; i++) {
                sb.append(partes[i].substring(0, 1).toUpperCase()).append(partes[i].substring(1));
            }
            return sb.toString();
        }
        return limpio.isEmpty() ? "raiz" : limpio;
    }
}