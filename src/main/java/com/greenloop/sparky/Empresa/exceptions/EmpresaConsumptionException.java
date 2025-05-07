package com.greenloop.sparky.Empresa.exceptions;

public class EmpresaConsumptionException extends RuntimeException {
    public EmpresaConsumptionException(Long id, String mensaje) {
        super("Error al obtener el consumo de la empresa con id " + id + ": " + mensaje);
    }
}