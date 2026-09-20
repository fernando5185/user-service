package com.ferave.userservice.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice 
public class GlobalErrorHandlerException extends ResponseEntityExceptionHandler {

    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
    
    public ProblemDetail alreadyExistsException (AlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setStatus(404);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }

    public ProblemDetail notFoundException (NotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setStatus(404);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }

    public ProblemDetail handleHttpRequestMethodNotSupported (HttpRequestMethodNotSupportedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
        problemDetail.setStatus(404);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }

    public ProblemDetail handleMissingPathVariable (MissingPathVariableException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setStatus(404);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }

    public ProblemDetail handleNoHandlerFoundException (NoHandlerFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setStatus(404);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }
}
