package tareas.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class Security {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public Security(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/registroia/**").authenticated()

                    .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER","ESTUDIANTE", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
