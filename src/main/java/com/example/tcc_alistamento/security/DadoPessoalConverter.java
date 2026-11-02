package com.example.tcc_alistamento.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Usada nos atributos CPF, RG e CEP do Usuario (com @Convert)
// O Hibernate chama esta classe sozinho, toda vez que grava ou lê esses campos no banco

@Converter
// Converte o texto do Java no texto do banco de dados (e de volta)
public class DadoPessoalConverter implements AttributeConverter<String, String> {

    // Criprtografa antes de gravar
    @Override
    public String convertToDatabaseColumn(String valor) {
        return Criptografia.criptografar(valor);
    }
    // Descriptografa ao ler
    @Override
    public String convertToEntityAttribute(String valorDoBanco) {
        return Criptografia.descriptografar(valorDoBanco);
    }
}
