package com.greenloop.sparky.Empresa.exceptions;

public class EmpresaValidationException extends RuntimeException {
    public EmpresaValidationException(String mensaje) {
        super(mensaje);
    }
}