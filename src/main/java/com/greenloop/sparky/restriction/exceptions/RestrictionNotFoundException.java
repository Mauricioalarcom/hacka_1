package com.greenloop.sparky.restriction.exceptions;

public class RestrictionNotFoundException extends RestrictionException {

    public RestrictionNotFoundException(Long id) {
        super("Restricción no encontrada con ID: " + id);
    }
}
