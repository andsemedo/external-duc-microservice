package com.andsemedodev.externalducmicroservice.exceptions;

import java.util.Map;

public class DucRequestValidatorException extends RuntimeException {
    private final Map<String, String> errors;

    public DucRequestValidatorException(Map<String, String> errors, String message) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
