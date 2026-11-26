package com.ferave.userservice.shared.exceptions.domain;

public class EmailAlreadyExistsException  extends RuntimeException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
