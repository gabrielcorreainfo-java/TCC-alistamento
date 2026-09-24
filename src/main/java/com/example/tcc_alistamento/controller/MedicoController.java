package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.LoginResponseDTO;
import com.example.tcc_alistamento.dto.MedicoRequestDTO;
import com.example.tcc_alistamento.dto.MedicoResponseDTO;
import com.example.tcc_alistamento.infra.security.TokenService;
import com.example.tcc_alistamento.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medico")
public class MedicoController {
    // Service responsável pelas regras de negócio do médico.
    private final MedicoService medicoService;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    TokenService tokenService;

    // Injeção de dependência da MedicoService.
    public MedicoController(MedicoService medicoService, PasswordEncoder passwordEncoder) {
        this.medicoService = medicoService;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint para cadastrar um novo médico.
    // Recebe os dados em JSON e envia para a Service salvar.
    @PostMapping
    public MedicoResponseDTO cadastrar(@RequestBody MedicoRequestDTO dto) {
        return medicoService.salvar(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody MedicoRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.emailMedico(), dto.senhaMedico());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserDetails) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token, "MEDICO"));
    }

    // Endpoint para listar todos os médicos cadastrados.
    @GetMapping
    public List<MedicoResponseDTO> listar() {
        return medicoService.listar();
    }

    // Endpoint para buscar médico pelo id.
    // PathVariable captura na URL o id.
    @GetMapping("/{id}")
    public MedicoResponseDTO buscarPorId(@PathVariable Integer id) {
        return medicoService.buscarPorId(id);
    }


    // Endpoint para atualizar todo registro do médico.
    @PutMapping("/{id}")
    public MedicoResponseDTO atualizarMedico(@PathVariable Integer id,
                                             @RequestBody MedicoRequestDTO dto) {
        return medicoService.atualizar(id, dto);
    }

    // Endpoint para atualizar parcialmente um registro.
    @PatchMapping("/{id}")
    public MedicoResponseDTO atualizarParcial(@PathVariable Integer id,
                                              @RequestBody MedicoRequestDTO dto) {
        return medicoService.atualizarParcial(id, dto);
    }

    // Endpoint para deletar médico.
    @DeleteMapping("/{id}")
    public MedicoResponseDTO deletarMedico(@PathVariable Integer id) {
        return medicoService.deletarMedico(id);
    }
}
