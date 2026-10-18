package com.example.tcc_alistamento.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class DadoPessoalConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String valor) {
        return Criptografia.criptografar(valor);
    }

    @Override
    public String convertToEntityAttribute(String valorDoBanco) {
        return Criptografia.descriptografar(valorDoBanco);
    }
}
