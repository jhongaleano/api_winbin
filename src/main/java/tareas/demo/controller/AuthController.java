package tareas.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tareas.demo.security.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String documento = loginRequest.get("documento");
            String password = loginRequest.get("password");

            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(documento, password)
            );

            String rol = auth.getAuthorities().iterator().next().getAuthority();

            String tokenReal = jwtService.generarToken(documento, rol);

            Map<String, String> response = new HashMap<>();
            response.put("token", tokenReal);
            response.put("documento", documento);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas: " + e.getMessage());
        }
    }
}