package com.example.tcc_alistamento.exceptions;

public class MedicoNotFoundException extends RuntimeException {
    public MedicoNotFoundException(String message) {
        super(message);
    }
}
