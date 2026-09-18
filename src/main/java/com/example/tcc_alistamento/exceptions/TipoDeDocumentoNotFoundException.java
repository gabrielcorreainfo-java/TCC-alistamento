package com.example.tcc_alistamento.exceptions;



public class TipoDeDocumentoNotFoundException extends RuntimeException{
    public TipoDeDocumentoNotFoundException (String mensagem){
        super(mensagem);
    }
}
