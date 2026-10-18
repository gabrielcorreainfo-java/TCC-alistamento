package com.example.tcc_alistamento.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.tcc_alistamento.security.UsuarioAutenticado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// Responsável por gerar e validar os tokens JWT
@Service
public class TokenService {

    private static final String EMISSOR = "alistamento-api";
    private static final long HORAS_DE_VALIDADE = 2;

    // Chave secreta que assina os tokens (variável de ambiente JWT_SECRET)
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UsuarioAutenticado usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant agora = Instant.now();

            return JWT.create()
                    .withIssuer(EMISSOR)                       // quem criou o token
                    .withSubject(usuario.getUsername())        // de quem é: o e-mail
                    .withClaim("role", usuario.getPerfil())    // perfil (só informativo)
                    .withIssuedAt(agora)                       // quando foi criado
                    .withExpiresAt(agora.plus(HORAS_DE_VALIDADE, ChronoUnit.HOURS)) // vence em 2h
                    .sign(algorithm);                          // assinatura

        } catch (JWTCreationException ex) {
            throw new RuntimeException("Erro ao gerar o token", ex);
        }
    }

    // Devolve o e-mail do token, ou "" se o token for falso, alterado, vencido ou de outro emissor
    public String ValidateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(EMISSOR)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }
}