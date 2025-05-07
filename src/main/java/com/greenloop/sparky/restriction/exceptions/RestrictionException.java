package com.greenloop.sparky.restriction.exceptions;

public class RestrictionException extends RuntimeException {

  public RestrictionException(String message) {
    super(message);
  }

  public RestrictionException(String message, Throwable cause) {
    super(message, cause);
  }
}
