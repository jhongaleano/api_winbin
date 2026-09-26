package tareas.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tareas.demo.config.OpenApiConstants;
import tareas.demo.dto.AuthResponse;
import tareas.demo.payload.LoginRequest;
import tareas.demo.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login y obtención de token JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = """
                    Valida credenciales y devuelve un JWT válido por 24 horas.

                    **Público** — no requiere token previo.

                    **Producción:** POST https://api-winbin.onrender.com/api/auth/login

                    Después del login, usa **Authorize** en Swagger UI con: `Bearer <token>`
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Documento o contraseña faltantes"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o cuenta desactivada")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String documento = loginRequest.getDocumento();
            String contrasenna = loginRequest.getContrasenna();

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

            return ResponseEntity.ok(new AuthResponse(tokenReal, documento));

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
