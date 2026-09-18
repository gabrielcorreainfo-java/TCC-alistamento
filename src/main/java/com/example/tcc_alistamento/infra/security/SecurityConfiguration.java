// toda infra (configuração) relaciona a minha segurança
package com.example.tcc_alistamento.infra.security;

// Classe de configuração de segurança da aplicação
// Destivando as funçõwa do Spring Security e colocando a nossa

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Classe que contém configurações da aplicação
@Configuration

// Habilita os recursos de segurança do web do Security
// e use configuração de segurança que eu definir aqui
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    Securityfilter securityfilter;

    // Pode instancia nossa classe
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                // desativa a proteção CRSF baseada na autentição por sessão
                .csrf(csrf -> csrf.disable())
                // Trabalha com a politica STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Raquisições HTTP que sejam atualizadas
                .authorizeHttpRequests(authorize -> authorize
                        // filtros para processar as requisições
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/administrador/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/medico/login").permitAll()
                        // O resto dos métodos só precisa apenas estar autenticado
                        .anyRequest().authenticated()
                )
                // Filtro para verificar o status do usuario antes de cair nos filtros dos endpoints
                .addFilterBefore(securityfilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    // AuthenticationManager é o componente responsável por tentar autenticar o usuário.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Método responsável pela criptografia da nossas senhas
    @Bean
    public PasswordEncoder passwordEncoder(){
        // Classe para fazer criptografia das senhas
        return new BCryptPasswordEncoder();
    }
}
