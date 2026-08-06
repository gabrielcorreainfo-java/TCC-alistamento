package com.example.tcc_alistamento.exceptions;

// Cria a exceção
public class AlistamentoNotFoundException extends RuntimeException{
    public AlistamentoNotFoundException(String mensagem){
        super(mensagem);
    }
}
