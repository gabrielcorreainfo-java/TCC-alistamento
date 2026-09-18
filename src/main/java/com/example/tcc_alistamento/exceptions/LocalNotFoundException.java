package com.example.tcc_alistamento.exceptions;

public class LocalNotFoundException extends RuntimeException{
    public LocalNotFoundException(String mensagem){
        super(mensagem);
    }
}
