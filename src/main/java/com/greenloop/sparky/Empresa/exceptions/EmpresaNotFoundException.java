package com.greenloop.sparky.Empresa.exceptions;

public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException(Long id) {
        super("Empresa con id " + id + " no encontrada");
    }
}