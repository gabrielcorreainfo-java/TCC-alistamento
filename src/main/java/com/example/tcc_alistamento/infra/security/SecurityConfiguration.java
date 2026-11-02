// toda infra (configuração) relaciona a minha segurança
package com.example.tcc_alistamento.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
  Classe responsáveç pela configuração central da segurança da API
  EnableWebSecurity -> Usa a regra de segurança que eu escrevi nesta classe
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String USER = "USER";
    private static final String MEDICO = "MEDICO";
    private static final String ADMIN = "ADMIN";

    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    // Metódo define os filtros de segurança da nossa API realizando validações
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Objeto injetado para configramos as requisições HTTP
        return httpSecurity
                // Desativa a proteção contra CSRF
                .csrf(csrf -> csrf.disable())
                // Aplica o CorsConfig (qual front pode chamar a API)
                .cors(Customizer.withDefaults())
                // Não armazena sessão no servidor: a identidade vem do token em cada requisição
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Definimos todas as autenticações das rotas
                .authorizeHttpRequests(authorize -> authorize

                        // ----- Públicas -----
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/recuperacao-senha").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cep/*").permitAll()

                        // ----- Cidadão -----
                        .requestMatchers(HttpMethod.POST, "/alistamento").hasAuthority(USER)
                        .requestMatchers(HttpMethod.POST, "/documentos").hasAuthority(USER)
                        .requestMatchers(HttpMethod.POST, "/documentos/*/arquivo").hasAuthority(USER)
                        .requestMatchers(HttpMethod.PATCH, "/agendamentos/*/confirmar").hasAuthority(USER)

                        // Cidadão (o próprio documento) ou administrador (ao excluir um cidadão)
                        .requestMatchers(HttpMethod.DELETE, "/documentos/*").hasAnyAuthority(USER, ADMIN)

                        // ----- Médico -----
                        .requestMatchers(HttpMethod.POST, "/avaliacoes-medicas").hasAuthority(MEDICO)

                        // ----- Listas completas (LGPD): só quem trabalha no processo -----
                        .requestMatchers(HttpMethod.GET,
                                "/usuario", "/alistamento", "/documentos",
                                "/agendamentos", "/avaliacoes-medicas").hasAnyAuthority(ADMIN, MEDICO)

                        // ----- Administrador -----
                        .requestMatchers(HttpMethod.GET, "/recuperacao-senha").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/recuperacao-senha/*/concluir").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.PUT, "/usuario/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/usuario/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/usuario/*").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.PUT, "/alistamento/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/alistamento/*").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.PUT, "/documentos/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/documentos/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/documentos/*/status").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/agendamentos/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/agendamentos/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/agendamentos/*").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.PUT, "/avaliacoes-medicas/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/avaliacoes-medicas/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/avaliacoes-medicas/*").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.POST, "/administrador", "/medico", "/local", "/tipos-documento").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/administrador/*", "/medico/*", "/local/*", "/tipos-documento/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/administrador/*", "/medico/*", "/local/*", "/tipos-documento/*").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/administrador/*", "/medico/*", "/local/*", "/tipos-documento/*").hasAuthority(ADMIN)

                        // Qualquer rota não citada só pode ser acessado por quem está logado
                        .anyRequest().authenticated()
                )
                // Usuário sem token não consegue acessar uma rota protegida
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                // Nosso filtro lê o token antes dos filtros padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                // Junta todas as regras e entrega toda a configuração
                .build();
    }

    // Componente usado para conferir email e senha para autenticação (usa o AuthenticationService e o BCrypt)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean -> Metódo de outra biblioteca (Spring Security)
    // BCrypt: hash irreversível das senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}