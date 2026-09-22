package com.example.tcc_alistamento.infra.security;

import com.example.tcc_alistamento.repository.UsuarioRepository;
import com.example.tcc_alistamento.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Componente da nossa aplicação
@Component

// Gera uma vez a cada requisição
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       var token = this.recoreyToken(request);
       if(token != null){
           var login = tokenService.ValidateToken(token);
           UserDetails user = authenticationService.loadUserByUsername(login);

           var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(authentication);
       }
       filterChain.doFilter(request, response);
    }
    private String recoreyToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authentization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
