package com.ferave.userservice.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> resourceNotFound(ResourceNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            exception.getMessage()
        );

        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @ExceptionHandler (AlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> alreadyExistsException(AlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            exception.getMessage()
        );

        problemDetail.setTitle("Recurso ya existe");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(null);

    }
}
