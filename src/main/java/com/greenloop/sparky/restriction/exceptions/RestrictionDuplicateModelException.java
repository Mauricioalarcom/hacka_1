package com.greenloop.sparky.restriction.exceptions;

public class RestrictionDuplicateModelException extends RestrictionException {

  public RestrictionDuplicateModelException(String model) {
    super("Ya existe una restricción con el modelo: " + model);
  }
}
