package tareas.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tareas.demo.services.ArduinoService;

import java.util.Map;

@RestController
@RequestMapping("/api/arduino")
public class ArduinoController {

    private final ArduinoService arduinoService;

    public ArduinoController(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    @GetMapping("/pendiente")
    public ResponseEntity<Map<String, Integer>> pendiente() {
        Integer id = arduinoService.siguienteOrden();
        if (id == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Map.of("idMaterial", id));
    }

}
