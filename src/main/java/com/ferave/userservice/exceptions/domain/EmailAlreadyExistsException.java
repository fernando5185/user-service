package com.ferave.userservice.exceptions.domain;

public class EmailAlreadyExistsException  extends RuntimeException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
