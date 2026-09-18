package com.example.tcc_alistamento.enums;

public enum RolesUsers {

    // Opção do enum que guarda admin
ADMIN("ADMIN"),
    // Opção do enum que guarda usuário
USER("USER"),
    // Opção do enum que guarda médico
MEDICO("MEDICO");

private String role;

RolesUsers(String role){
    this.role=role;
}

public String getRole(){
    return role;
  }
}
