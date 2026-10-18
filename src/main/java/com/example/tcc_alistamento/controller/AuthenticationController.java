package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.LoginRequestDTO;
import com.example.tcc_alistamento.dto.LoginResponseDTO;
import com.example.tcc_alistamento.infra.security.TokenService;
import com.example.tcc_alistamento.security.UsuarioAutenticado;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Login único para cidadão, médico e administrador.
Confere e-mail e senha (BCrypt) e devolve o token JWT com o perfil, o id e o nome.
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        // 1. Confere e-mail e senha (BCrypt). Se estiver errado, o GlobalExceptionHandler responde 401.
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        var auth = authenticationManager.authenticate(usernamePassword);

        // 2. Quem entrou: UsuarioDetails, MedicoDetails ou AdministradorDetails
        UsuarioAutenticado usuario = (UsuarioAutenticado) auth.getPrincipal();

        // 3. Gera o token e devolve junto com perfil, id e nome
        String token = tokenService.generateToken(usuario);

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                usuario.getPerfil(),
                usuario.getId(),
                usuario.getNome()
        ));
    }
}