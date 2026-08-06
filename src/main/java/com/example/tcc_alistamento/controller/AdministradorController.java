package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.entity.Administrador;

import com.example.tcc_alistamento.service.AdministradorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {
    private final AdministradorService administradorService;

    public AdministradorController (AdministradorService administradorService) {
    this.administradorService = administradorService;
    }

    @PostMapping
    public Administrador cadastrar(@RequestBody Administrador administrador){
        return administradorService.salvar(administrador);
    }

    @PostMapping("/login")
    public Administrador login(@RequestBody Administrador administrador){
        return administradorService.login(administrador.getEmailAdmin(), administrador.getSenhaAdmin());
    }

    @GetMapping
    public List<Administrador> listar(){
        return administradorService.listar();
    }

    @GetMapping("/{id}")
    public Administrador buscarPorId(@PathVariable  Integer id){
        return administradorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Administrador atualizar(@PathVariable  Integer id , @RequestBody Administrador administrador){
        return administradorService.atualizar(id, administrador);

    }

    @PatchMapping("/{id}")
    public Administrador atualizarParcial(@PathVariable  Integer id , @RequestBody Administrador administrador ){
       return administradorService.atualizarParcial(id, administrador);

    }

    @DeleteMapping("/{id}")
    public Administrador deletar(@PathVariable Integer id){
        return administradorService.deletar(id);
    }

}

