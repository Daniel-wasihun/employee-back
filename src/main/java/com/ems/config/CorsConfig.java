package com.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        String frontendUrl = System.getenv("FRONTEND_URL");
        List<String> allowedOrigins = new java.util.ArrayList<>(List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:3001",
                "https://localhost",
                "https://localhost:8444",
                "https://localhost:8443",
                "https://localhost:3001",
                "https://employee-front-275b.onrender.com"
        ));
        if (frontendUrl != null && !frontendUrl.isEmpty()) {
            allowedOrigins.add(frontendUrl);
        }
        
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
