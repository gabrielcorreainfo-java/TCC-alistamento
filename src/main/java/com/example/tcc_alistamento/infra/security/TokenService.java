package com.example.tcc_alistamento.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.tcc_alistamento.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Responsável pela geração de token
@Service
public class TokenService {

    // Variavel de ambiente
    @Value("${api.security.token.secret}")
    private String secret;


    //  Parametro usuário para saber no token qual é o usuário autenticado e sua role
    public String generateToken(UserDetails userDetails ){
        try{
            /*
            algoritmo geração de token
            algorithm recebe por parametro uma secret
            Secret é chave privada que o seu backend usa para assinar e conferir se os JWTs são legítimos.
            Sendo um fator imporatnte para a geração dos nossos tokens
             */

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    // Quem criou o token
                    .withIssuer("alistamento-api")
                    // Entidade que vai receber
                    .withSubject(userDetails.getUsername())
                    // Tempo de expiração do token
                    .withExpiresAt(getExpiretionDate())
                    // fazer assinatura e a geração final
                    .sign(algorithm);
            return token;

            // quando nenhum dos parametros nao derem certo ele faz essa exceção
        }catch(JWTCreationException ex ){
            throw new RuntimeException("Error while generating token", ex);
        }
    }

    public String ValidateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("alistamento-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException ex ){
            return "";
        }
    }

    // expiresAlt recebe um Instant
    private Instant getExpiretionDate(){
        // Pegou o tempo de agora adicionou 2H e transformou em um Instant e colocou no nosso time Zone de Brasilia
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
