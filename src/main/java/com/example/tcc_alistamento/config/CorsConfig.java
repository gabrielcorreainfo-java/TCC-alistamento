package com.example.tcc_alistamento.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
CORS é uma regra de segurança que existe nos navegadores
para controlar quem pode acessar uma API.
 */
@Configuration
public class CorsConfig {

    // Cria objeto que o Spring gerencia
    // CorsFilter ele filtra o caminho da requisição, verificando se ela pode entrar
    @Bean
    public CorsFilter corsFilter(){

        // Guardar as configurações CORS
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        //  cria a configuração
        CorsConfiguration config = new CorsConfiguration();

        // Permite o Angular acessar a API
        config.addAllowedOrigin("http://localhost:4200");

        // Permiti o uso de todos os métodos HTTP
        config.addAllowedMethod("*");

        // Permite todos os headers
        config.addAllowedHeader("*");

        // Aplica a configuração para todas as rotas da API
        source.registerCorsConfiguration("/**",config);

        // cria e devvolve o filtro para o Spring usar
        return new CorsFilter(source);
    }

}
