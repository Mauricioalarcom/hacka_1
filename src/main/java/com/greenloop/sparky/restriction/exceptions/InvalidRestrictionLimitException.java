package com.greenloop.sparky.restriction.exceptions;

public class InvalidRestrictionLimitException extends RestrictionException {

    public InvalidRestrictionLimitException(Integer limit) {
        super("El límite de restricción no es válido: " + limit);
    }
}
