package com.example.tcc_alistamento.security.auth;

import com.example.tcc_alistamento.dto.request.LoginRequestDTO;
import com.example.tcc_alistamento.dto.response.LoginResponseDTO;
import com.example.tcc_alistamento.security.jwt.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Login único para cidadão, médico e administrador.
Confere e-mail e senha e devolve o token JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {

        //Confere e-mail e senha. Se estiver errado, o GlobalExceptionHandler responde 401.
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        //  Autentica a entidade  passando o usernamePassword verificando quem é essa pessoa
        var auth = authenticationManager.authenticate(usernamePassword);

        //  Pega quem fez login para ler o id, o nome e o perfil
        UsuarioAutenticado entidadesLogando = (UsuarioAutenticado) auth.getPrincipal();

        // Gera o token e devolve junto com perfil, id e nome
        String token = tokenService.generateToken(entidadesLogando);

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                entidadesLogando.getPerfil(),
                entidadesLogando.getId(),
                entidadesLogando.getNome()
        ));
    }
}