package com.example.tcc_alistamento.exceptions;

public class DocumentoNotFoundException extends RuntimeException{
    public DocumentoNotFoundException(String mensagem) {
        super(mensagem);
    }
}
