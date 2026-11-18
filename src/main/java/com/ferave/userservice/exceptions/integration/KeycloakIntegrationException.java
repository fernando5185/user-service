package com.ferave.userservice.exceptions.integration;

public class KeycloakIntegrationException extends RuntimeException {
    
    public KeycloakIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeycloakIntegrationException(String message) {
        super(message);
    }

    public KeycloakIntegrationException(Throwable cause) {
        super(cause);
    }

    
}
