package com.comerbien.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.comerbien.backend.security.JwtAuthenticationFilter;
import com.comerbien.backend.security.CustomUserDetailsService;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("🔧 Configuring Spring Security...");
        System.out.println("🔧 Configuring Spring Security with paths:");
        System.out.println("   - /api/auth/** → PERMIT_ALL");
        System.out.println("   - /api/health → PERMIT_ALL");
        System.out.println("   - /api/recipes/public → PERMIT_ALL");
        System.out.println("   - /api/recipes/search → PERMIT_ALL");
        System.out.println("   - All others → AUTHENTICATED");

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // ⚠️ IMPORTANTE: Usar paths COMPLETOS incluyendo /api
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/api/recipes/public").permitAll()
                        .requestMatchers("/api/recipes/search").permitAll()
                        .requestMatchers("/api/test").permitAll()
                        .requestMatchers("/api/favorites/*/status").permitAll()
                        .requestMatchers("/api/files/*").permitAll()
                        .requestMatchers("/uploads/*").permitAll()
                        .requestMatchers("/api/menus/*").authenticated() // Todos los endpoints de menús requieren
                                                                         // auth// Para ver status sin auth. Si tienes
                                                                         // endpoint de test
                        .requestMatchers("/api/food-facts/*").permitAll() // ✅ NUEVO: Food facts públicos

                        // Todos los demás endpoints requieren autenticación
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("✅ Spring Security configured successfully");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("🔧 Configuring CORS...");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "https://comerbien.com.mx",
                "https://www.comerbien.com.mx",
                "https://tourmaline-sprite-043ae0.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        System.out.println("✅ CORS configured for: http://localhost:8080");
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}