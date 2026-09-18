package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AdministradorAlistamentosResponseDTO;
import com.example.tcc_alistamento.dto.AdministradorRequestDTO;
import com.example.tcc_alistamento.dto.AdministradorResponseDTO;
import com.example.tcc_alistamento.dto.LoginResponseDTO;
import com.example.tcc_alistamento.infra.security.TokenService;
import com.example.tcc_alistamento.service.AdministradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {
    private final AdministradorService administradorService;
    TokenService tokenService;
    AuthenticationManager authenticationManager;


    public AdministradorController (AdministradorService administradorService) {
    this.administradorService = administradorService;
    }

    @PostMapping
    public AdministradorResponseDTO cadastrar(@RequestBody AdministradorRequestDTO dto){
        return administradorService.salvar(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AdministradorRequestDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.emailAdmin(), dto.senhaAdmin());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserDetails) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping
    public List<AdministradorResponseDTO> listar(){
        return administradorService.listar();
    }

    @GetMapping("/{id}")
    public AdministradorResponseDTO buscarPorId(@PathVariable  Integer id){
        return administradorService.buscarPorId(id);
    }

    @GetMapping("/{id}/alistamentos")
    public AdministradorAlistamentosResponseDTO buscarAlistamentos(@PathVariable Integer id){
        return administradorService.buscarAlistamentos(id);
    }

    @PutMapping("/{id}")
    public AdministradorResponseDTO atualizar(@PathVariable  Integer id , @RequestBody AdministradorRequestDTO dto){
        return administradorService.atualizar(id,dto);

    }

    @PatchMapping("/{id}")
    public AdministradorResponseDTO atualizarParcial(@PathVariable  Integer id , @RequestBody AdministradorRequestDTO dto ){
       return administradorService.atualizarParcial(id,dto);

    }

    @DeleteMapping("/{id}")
    public AdministradorResponseDTO deletar(@PathVariable Integer id){
        return administradorService.deletar(id);
    }


}

