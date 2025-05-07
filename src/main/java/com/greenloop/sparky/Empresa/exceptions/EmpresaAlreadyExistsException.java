package com.greenloop.sparky.Empresa.exceptions;

public class EmpresaAlreadyExistsException extends RuntimeException {
    public EmpresaAlreadyExistsException(String nombre) {
        super("Ya existe una empresa con el nombre: " + nombre);
    }
    
    public EmpresaAlreadyExistsException(Long id) {
        super("Ya existe una empresa con el id: " + id);
    }
}