package com.example.tcc_alistamento.exceptions;

public class AdministradorNotFoundException extends RuntimeException{
    public AdministradorNotFoundException(String mensagem){
        super(mensagem);
    }
}
