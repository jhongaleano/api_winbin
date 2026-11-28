package tareas.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tareas.demo.controller.AuthController;
import tareas.demo.controller.CategoriaPuntajeController;
import tareas.demo.dto.AuthResponse;
import tareas.demo.models.CategoriaPuntaje;
import tareas.demo.payload.LoginRequest;
import tareas.demo.repository.CategoriaPuntajeRepository;
import tareas.demo.security.JwtService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WinBinUnitariasTest {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private AuthController authController;

    private CategoriaPuntajeRepository repositorio;
    private CategoriaPuntajeController categoriaController;

    @BeforeEach
    void setUp() {
        // 1. Inicializar mocks y controlador de Auth
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtService = Mockito.mock(JwtService.class);
        authController = new AuthController(authenticationManager, jwtService);
        ReflectionTestUtils.setField(authController, "authenticationManager", authenticationManager);
        ReflectionTestUtils.setField(authController, "jwtService", jwtService);

        // 2. Inicializar mock y controlador de CategoriaPuntaje
        repositorio = Mockito.mock(CategoriaPuntajeRepository.class);
        categoriaController = new CategoriaPuntajeController(repositorio);

        ReflectionTestUtils.setField(categoriaController, "repositorio", repositorio);
    }


    @Test
    @DisplayName("CP-01: AuthService.login con credenciales válidas devuelve JWT")
    void testCp01LoginExitoso() {
        LoginRequest request = new LoginRequest();
        request.setDocumento("0000000000");
        request.setContrasenna("clave_prueba");

        // 2. Simular autenticación exitosa y generación de token
        Authentication authSimulada = new UsernamePasswordAuthenticationToken(
                "0000000000",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authSimulada);
        when(jwtService.generarToken("0000000000", "ROLE_ADMIN"))
                .thenReturn("eyJhbGciOiJIUzI1NiJ9.token_jwt_prueba");

        // 3. Ejecutar el método del Controller
        ResponseEntity<?> response = authController.login(request);

        // 4. Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(AuthResponse.class, response.getBody());

        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertTrue(authResponse.getToken().startsWith("eyJ"));

        System.out.println("CP-01 OK | Status: " + response.getStatusCode() + " | Token: " + authResponse.getToken());
    }

    @Test
    @DisplayName("CP-02: AuthController.login con contraseña incorrecta devuelve 401 Unauthorized")
    void testCp02LoginContrasenaMala() {
        // 1. Preparar credenciales inválidas
        LoginRequest request = new LoginRequest();
        request.setDocumento("0000000000");
        request.setContrasenna("mala");

        // 2. Simular que Spring Security rechaza la contraseña con BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // 3. Ejecutar el método del Controller
        ResponseEntity<?> response = authController.login(request);

        // 4. Verificar que tu catch devuelve HTTP 401 UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales incorrectas: documento o contraseña inválidos.", response.getBody());

        System.out.println("CP-02 OK | Status: " + response.getStatusCode() + " | Mensaje: " + response.getBody());
    }

    @Test
    @DisplayName("CP-04: Actualizar puntaje invoca el registro de auditoría una vez")
    void testCp04ActualizarPuntajeYAuditoria() {
        // 1. Preparar registro existente en BD simulada
        CategoriaPuntaje existente = new CategoriaPuntaje();
        existente.setPuntos(5);

        // 2. Preparar objeto con los cambios del @RequestBody
        CategoriaPuntaje cambios = new CategoriaPuntaje();
        cambios.setPuntos(10);

        when(repositorio.findById(1)).thenReturn(Optional.of(existente));
        when(repositorio.save(any(CategoriaPuntaje.class))).thenAnswer(inv -> inv.getArgument(0));

        // 3. Ejecutar el método del Controller
        ResponseEntity<CategoriaPuntaje> response = categoriaController.actualizar(1, cambios);

        // 4. Verificar respuesta HTTP 200, puntos actualizados y llamada única a save()
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getPuntos());
        verify(repositorio, times(1)).save(existente);

        System.out.println("CP-04 OK | Status: " + response.getStatusCode() + " | Nuevos puntos: " + response.getBody().getPuntos());
    }






}
