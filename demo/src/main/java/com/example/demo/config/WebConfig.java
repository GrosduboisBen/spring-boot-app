package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // toutes les routes exposées
                        .allowedOrigins("http://localhost:4200") // ton app Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ce que tu veux autoriser
                        .allowedHeaders("*") // tous les headers
                        .allowCredentials(true); // si tu comptes envoyer cookies ou auth
            }
        };
    }
}
