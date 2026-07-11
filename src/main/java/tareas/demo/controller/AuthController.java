package tareas.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tareas.demo.security.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

   private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String documento = loginRequest.get("documento");
            String contrasenna = loginRequest.get("contrasenna");

            if (documento == null || contrasenna == null) {
                return ResponseEntity.badRequest().body("El documento y la contraseña son obligatorios");
            }

            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(documento, contrasenna)
            );

            String rol = auth.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .orElse("ROLE_USER");

            String tokenReal = jwtService.generarToken(documento, rol);

            Map<String, String> response = new HashMap<>();
            response.put("token", tokenReal);
            response.put("documento", documento);

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("La cuenta de usuario se encuentra desactivada.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas: documento o contraseña inválidos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error al iniciar sesión: " + e.getMessage());
        }
    }
}