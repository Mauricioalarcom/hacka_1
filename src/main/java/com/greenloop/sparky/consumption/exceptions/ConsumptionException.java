package com.greenloop.sparky.consumption.exceptions;

public class ConsumptionException extends RuntimeException {

    public ConsumptionException(String message) {
        super(message);
    }

    public ConsumptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
