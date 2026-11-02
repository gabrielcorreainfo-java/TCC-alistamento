package com.example.tcc_alistamento.security.jwt;

import com.example.tcc_alistamento.security.auth.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Componente da nossa aplicação
@Component

// Gera uma vez a cada requisição
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    // O Spring entrega as duas classes pelo construtor
    public SecurityFilter(TokenService tokenService, AuthenticationService authenticationService) {
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pega o token do cabeçalho "Authorization" (ou null se não veio)
       var token = this.recoreyToken(request);

       if(token != null) {
           // Retorna o e-mail do token, ou "" se o token for inválido ou estiver vencido
           var login = tokenService.ValidateToken(token);
           // Só continua se a requisição trouxe um token
           if (!login.isEmpty()) {
               try {
                   // Busca a pessoa no banco
                   UserDetails user = authenticationService.loadUserByUsername(login);
                   // Monta o registro de quem está logado: a pessoa, sem senha (null) e o perfil
                   var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                   // Guarda quem está logado durante esta requisição
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               } catch (UsernameNotFoundException ex) {
                   // O usuário do token foi apagado: a requisição segue sem autenticação
               }
           }
       }
       filterChain.doFilter(request, response);
    }
    // Lê o token do cabeçalho "Authorization: Bearer <token>"
    private String recoreyToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        // Corta o "Bearer (7 caracteres) e devolve só o token
        return authHeader.substring(7);
    }
}
