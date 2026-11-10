package com.ferave.userservice.exceptions;

public class KeycloakUserAlreadyExistsException extends RuntimeException {

    public KeycloakUserAlreadyExistsException(String message) {
        super(message);
    }
}
