package com.greenloop.sparky.restriction.exceptions;



public class RestrictionNotOwnerException extends RestrictionException {

    public RestrictionNotOwnerException(Long id, Long empresaId) {
        super("La restricción con ID " + id + " no pertenece a la empresa con ID " + empresaId);
    }
}
