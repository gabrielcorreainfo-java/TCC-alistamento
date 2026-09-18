package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.dto.LoginResponseDTO;
import com.example.tcc_alistamento.dto.UsuarioRequestDTO;
import com.example.tcc_alistamento.dto.UsuarioResponseDTO;
import com.example.tcc_alistamento.infra.security.TokenService;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import com.example.tcc_alistamento.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 Controller responsável por receber as requisições relacionadas aos usuários.
 Faz a comunicação entre o cliente e a camada Service.
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;


    // Service responsável pelas regras de negócio do usuário.
    private final UsuarioService usuarioService;
    private final UsuarioRepository repository;

    // Injeção de dependência da UsuarioService.
    public UsuarioController(UsuarioService usuarioService, UsuarioRepository repository){
        this.usuarioService = usuarioService;
        this.repository = repository;
    }

    // Endpoint para cadastrar um novo usuário.
    // Recebe os dados em JSON e envia para a Service salvar.
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto ){
        if (repository.findByEmail(dto.email()) != null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(usuarioService.salvar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UsuarioRequestDTO dto){
        // Verificar se a senha corresponde a que está no BD
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(),dto.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // Endpoint para listar todos os usuários cadastrados.
    @GetMapping
    public List<UsuarioResponseDTO> listar(){
        return usuarioService.listarTodos();
    }

    // Endpoint para buscar usuario pelo id.
    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarPorId(@PathVariable Integer id){
        return usuarioService.buscarPorId(id);
    }

    // Endpoint para buscar alistamento por meio do Usuário
    @GetMapping("/{id}/alistamento")
    public AlistamentoResponseDTO buscarAlistamento(@PathVariable Integer id){
        return usuarioService.buscarAlistamentoDoUsuario(id);
    }

    // Endpoint para atualizar todo registro do usuario
    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioRequestDTO dto){
        return usuarioService.atualizarUsuario(id,dto);
    }

    // Emdpoint para atualizar parcialmente um registro
    @PatchMapping("/{id}")
    public UsuarioResponseDTO atualizarParcial( @PathVariable Integer id, @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.atualizarParcial(id,dto);
    }

    // Endpoint para deletar usuário
    @DeleteMapping("/{id}")
    public UsuarioResponseDTO deletarUsuario(@PathVariable Integer id) {
        return usuarioService.deletarUsuario(id);
    }
}

