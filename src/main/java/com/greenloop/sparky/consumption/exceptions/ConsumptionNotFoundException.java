package com.greenloop.sparky.consumption.exceptions;

public class ConsumptionNotFoundException extends ConsumptionException {

    public ConsumptionNotFoundException(Long id) {
        super("Registro de consumo no encontrado con ID: " + id);
    }
}
