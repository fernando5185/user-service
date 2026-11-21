package com.ferave.userservice.infrastructure.exception.integration;

public class KeycloakUserAlreadyExistsException extends RuntimeException {

    public KeycloakUserAlreadyExistsException(String message) {
        super(message);
    }

    public KeycloakUserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
