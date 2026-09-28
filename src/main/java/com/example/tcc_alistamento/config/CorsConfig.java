package com.example.tcc_alistamento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/*
CORS é uma regra de segurança que existe nos navegadores
para controlar quem pode acessar uma API.

Esta configuração é aplicada pelo Spring Security
(no SecurityConfiguration, pelo .cors(...)).
 */
@Configuration
public class CorsConfig {

    // Endereços do front que podem acessar a API, separados por vírgula
    @Value("${api.cors.origens}")
    private String origens;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        // Cria a configuração
        CorsConfiguration config = new CorsConfiguration();

        // Permite os endereços do front configurados
        config.setAllowedOrigins(Arrays.stream(origens.split(","))
                .map(String::trim)
                .toList());

        // Permite o uso de todos os métodos HTTP
        config.addAllowedMethod("*");

        // Permite todos os headers (inclusive o Authorization com o token)
        config.addAllowedHeader("*");

        // Aplica a configuração para todas as rotas da API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}