package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.entity.Usuario;
import com.example.tcc_alistamento.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 Controller responsável por receber as requisições relacionadas aos usuários.
 Faz a comunicação entre o cliente e a camada Service.
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    // Service responsável pelas regras de negócio do usuário.
    private final UsuarioService usuarioService;

    // Injeção de dependência da UsuarioService.
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    // Endpoint para cadastrar um novo usuário.
    // Recebe os dados em JSON e envia para a Service salvar.
    @PostMapping
    public Usuario cadastrar(@RequestBody Usuario usuario){
        return usuarioService.salvar(usuario);

    }

    // Endpoint para listar todos os usuários cadastrados.
    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.listarTodos();
    }

    // Endpoint para buscar usuario pelo id.
    // PathVariable captura na URL no id
    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Integer id){
        return usuarioService.buscarPorId(id);
    }

    // Endpoint para atualizar todo registro do usuario
    @PutMapping("/{id}")
    public Usuario atualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario){
        return usuarioService.atualizarUsuario(id,usuario);
    }

    // Emdpoint para atualizar parcialmente um registro
    @PatchMapping("/{id}")
    public Usuario atualizarParcial( @PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioService.atualizarParcial(id, usuario);
    }

    // Endpoint para deletar usuário
    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Integer id) {
        usuarioService.deletarUsuario(id);
    }
}

