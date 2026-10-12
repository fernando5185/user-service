package com.ferave.userservice.exceptions;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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

        problemDetail.setTitle("Resource not found");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler (EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> alreadyExistsException(EmailAlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, 
            exception.getMessage()
        );

        problemDetail.setTitle("The email is already registered");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.CONFLICT)
                .body(problemDetail);

    }

    @ExceptionHandler (HttpMessageNotReadableException.class)
    protected ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            exception.getMessage()
        );

        problemDetail.setTitle("Invalid JSON or unreadable request body");
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

        problemDetail.setTitle("Method Not Supported");
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

        problemDetail.setTitle("Missing parameter " + exception.getParameterName());
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

        problemDetail.setTitle("Unsupported content type");
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

        problemDetail.setTitle("Parameter is missing");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler (TypeMismatchException.class)
    protected ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException (TypeMismatchException exception) {
         ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            exception.getMessage()
        );

        problemDetail.setTitle("A parameter has the wrong type");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(problemDetail);
    }

    @ExceptionHandler (HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<ProblemDetail> handleHttpMediaTypeNotAcceptableException (HttpMediaTypeNotAcceptableException exception) {
         ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, 
            exception.getMessage()
        );

        problemDetail.setTitle("An acceptable response for the client can't be produced.");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity.
                status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler (AuthenticationException.class)
    protected ResponseEntity<ProblemDetail> handleAuthenticationException (AuthenticationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            exception.getMessage()
        );

        problemDetail.setTitle("The user is not authenticated or the credentials are not valid");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }

    @ExceptionHandler (AccessDeniedException.class)
    protected ResponseEntity<ProblemDetail> handleAccessDeniedException (AccessDeniedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN,
            exception.getMessage()
        );

        problemDetail.setTitle("The authenticated user doesn't have enough permissions");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(problemDetail);
    }

    @ExceptionHandler (InvalidBearerTokenException.class)
    protected ResponseEntity<ProblemDetail> handleInvalidBearerTokenException (InvalidBearerTokenException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN,
            exception.getMessage()
        );

        problemDetail.setTitle("The Bearer token is not valid");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(problemDetail);
    }

    @ExceptionHandler (InsufficientAuthenticationException.class)
    protected ResponseEntity<ProblemDetail> handleInsufficientAuthenticationException (InsufficientAuthenticationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            exception.getMessage()
        );

        problemDetail.setTitle("Authentication is insufficient to access the resource");
        problemDetail.setProperty("Timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }
}
