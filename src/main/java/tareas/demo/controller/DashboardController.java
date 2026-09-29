package tareas.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.dto.DashboardResumenDTO;
import tareas.demo.services.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Panel de estadísticas y métricas")
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @GetMapping("/resumen-completo")
    @SecurityRequirement(name = OpenApiConstants.BEARER_AUTH)
    public ResponseEntity<DashboardResumenDTO> getResumen() {
       return ResponseEntity.ok(dashboardService.obtenerResumenCompleto());
    }
}
