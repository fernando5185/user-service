package com.ferave.userservice.shared.exceptions.integration;

public class KeycloakUserAlreadyExistsException extends RuntimeException {

    public KeycloakUserAlreadyExistsException(String message) {
        super(message);
    }

    public KeycloakUserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
