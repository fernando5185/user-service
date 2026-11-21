package com.ferave.userservice.domain.exceptions;

public class UserCreateException extends RuntimeException {
    
    public UserCreateException(String message) {
        super(message);
    }

    public UserCreateException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
