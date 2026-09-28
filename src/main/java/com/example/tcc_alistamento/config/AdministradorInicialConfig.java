package com.example.tcc_alistamento.config;

import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


/*
Cria o primeiro administrador automaticamente quando a API sobe
e ainda não existe nenhum administrador no banco.

Isso é necessário porque a rota POST /administrador exige ser ADMIN,
então sem esta classe ninguém conseguiria criar o primeiro.
 */

@Configuration
public class AdministradorInicialConfig {
    private static final Logger log = LoggerFactory.getLogger(AdministradorInicialConfig.class);

    @Bean
    public CommandLineRunner criarAdministradorInicial(
            AdministradorRepository administradorRepository,
            PasswordEncoder passwordEncoder,
            @Value("${api.admin.nome}") String nome,
            @Value("${api.admin.email}") String email,
            @Value("${api.admin.senha}") String senha) {

        return args -> {
            // Já existe administrador: não faz nada
            if (administradorRepository.count() > 0) {
                return;
            }

            // Sem senha configurada, não cria (para não deixar uma senha fixa no código)
            if (senha == null || senha.isBlank()) {
                log.warn("Nenhum administrador cadastrado. Defina a variável ADMIN_SENHA para criar o primeiro automaticamente.");
                return;
            }

            Administrador administrador = new Administrador();
            administrador.setNomeAdmin(nome);
            administrador.setEmailAdmin(email);
            administrador.setSenhaAdmin(passwordEncoder.encode(senha));
            administradorRepository.save(administrador);

            log.info("Administrador inicial criado: {}", email);
        };
    }
}
