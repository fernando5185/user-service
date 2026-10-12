package com.ferave.userservice.exceptions;

public class EmailAlreadyExistsException  extends RuntimeException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
