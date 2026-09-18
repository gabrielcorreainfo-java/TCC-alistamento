package com.example.tcc_alistamento.exceptions;

public class AgendamentoNotFoundException extends RuntimeException {
    public AgendamentoNotFoundException(String message) {
        super(message);
    }
}
