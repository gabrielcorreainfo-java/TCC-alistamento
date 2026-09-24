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
    SecurityFilter securityfilter;

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
                        // Público
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/administrador/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/medico/login").permitAll()

                        // Usuário
                        .requestMatchers(HttpMethod.POST, "/alistamento").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/usuario/*/alistamento").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/alistamento/*/completo").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/documentos").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/documentos/*").hasAuthority("USER")
                        .requestMatchers(HttpMethod.PATCH, "/agendamentos/*/confirmar").hasAuthority("USER")

                        // Administrador
                        .requestMatchers(HttpMethod.PUT, "/usuario/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/usuario/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/alistamento/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/administrador").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/medico").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/medico/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/medico/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/documentos/*/status").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/avaliacoes-medicas/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/avaliacoes-medicas/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/avaliacoes-medicas/*").hasAuthority("ADMIN")

                        // Médico
                        .requestMatchers(HttpMethod.POST, "/avaliacoes-medicas").hasAuthority("MEDICO")


                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()


                        // Resto: só precisa estar autenticado (algum dos 3 papéis)
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
