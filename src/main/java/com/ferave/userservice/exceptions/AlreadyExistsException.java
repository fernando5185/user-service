package com.ferave.userservice.exceptions;

public class AlreadyExistsException  extends RuntimeException {
    
    public AlreadyExistsException(String message) {
        super(message);
    }
}
