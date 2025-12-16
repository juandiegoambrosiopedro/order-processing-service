package mx.com.leyva.config.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.leyva.config.security.properties.SecurityPathsProperties;
import mx.com.leyva.order.exception.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad de la aplicación.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /** Servicio que contiene las rutas definidas en configuración. */
    private final SecurityPathsProperties securityPathsProperties;

    /** Servicio que maneja las solicitudes no autenticadas en la API. */
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /** Imprime rutas permitidas al iniciar la aplicación. */
    @PostConstruct
    public void printAllowedPaths() {
        securityPathsProperties.getIgnoring().forEach(rule ->
                log.info("{} ", rule.getPath())
        );
    }

    /** Configura la cadena de seguridad HTTP, incluyendo autenticación, autorización y manejo de 401. */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                // Permitir frames para H2 console
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )
                .authorizeHttpRequests(auth -> {
                    securityPathsProperties.getIgnoring().forEach(rule -> {
                        auth.requestMatchers(rule.getPath()).permitAll();
                    });
                    auth.anyRequest().authenticated();
                })
                .httpBasic(basic -> {})
                .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint));

        return http.build();
    }

    /** Usuario en memoria. */
    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin123"))
                        .roles("ADMIN")
                        .build()
        );
    }

    /** PasswordEncoder único. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
