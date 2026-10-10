package com.ferave.userservice.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j 
public class GlobalErrorHandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler (MethodArgumentNotValidException.class)
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
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            exception.getMessage()
        );

        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler (AlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> alreadyExistsException(AlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            exception.getMessage()
        );

        problemDetail.setTitle(exception.getMessage());
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);

    }

    @ExceptionHandler (HttpMessageNotReadableException.class)
    protected ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            exception.getMessage()
        );

        problemDetail.setTitle("Json invalido o cuerpo de petición ilegible");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler (HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ProblemDetail> handleHttpRequestMethodNotSupport(HttpRequestMethodNotSupportedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.METHOD_NOT_ALLOWED, 
            exception.getMessage()
        );

        problemDetail.setTitle("Metodo Http no permitido");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(problemDetail);
    }

    @ExceptionHandler (MissingServletRequestParameterException.class)
    protected ResponseEntity<ProblemDetail> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            exception.getMessage()
        );

        problemDetail.setTitle("Metodo Http no permitido");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler (HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ProblemDetail> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            exception.getMessage()
        );

        problemDetail.setTitle("Tipo de contenido no soportado");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(problemDetail);
    }

    @ExceptionHandler (MissingPathVariableException.class)
    protected ResponseEntity<ProblemDetail> handleMissingPathVariableException(MissingPathVariableException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            exception.getMessage()
        );

        problemDetail.setTitle("Tipo de contenido no soportado");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }
}
