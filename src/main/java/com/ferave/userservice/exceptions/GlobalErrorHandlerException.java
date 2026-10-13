package com.ferave.userservice.exceptions;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j 
public class GlobalErrorHandlerException extends ResponseEntityExceptionHandler {

    private ProblemDetail createProblemDetail(
        HttpStatusCode status,
        String title,
        String detail
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more validation errors occurred"
        );

        problemDetail.setTitle("Validation failed");
        problemDetail.setProperty("Timestamp", Instant.now());
        problemDetail.setProperty("Errors", errors);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException exception) {
        createProblemDetail(
            HttpStatus.NOT_FOUND, 
            "Resource not Found", 
            exception.getMessage());

        return ResponseEntity.
                status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler (EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> alreadyExistsException(EmailAlreadyExistsException exception) {
        createProblemDetail(
            HttpStatus.CONFLICT, 
            "Already resource exists",
            exception.getMessage());

        return ResponseEntity.
                status(HttpStatus.CONFLICT).build();

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "Invalid JSON or unreadable request body",
            exception.getMessage());
    

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        createProblemDetail(
            HttpStatus.METHOD_NOT_ALLOWED, 
            "Method Not Supported",
            exception.getMessage());
        
        return ResponseEntity.
                status(HttpStatus.METHOD_NOT_ALLOWED)
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "Missing Parameter",
            exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        createProblemDetail(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            "Unsupported content type",
            exception.getMessage());

        return ResponseEntity.
                status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Parameter is missing",
            exception.getMessage());

        return ResponseEntity.
                status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

     @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        
        createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "A parameter has the wrong type",
            exception.getMessage());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
                
        createProblemDetail(
            HttpStatus.NOT_ACCEPTABLE, 
            "An acceptable response for the client can't be produced.",
            exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    @ExceptionHandler (AuthenticationException.class)
    protected ResponseEntity<ProblemDetail> handleAuthenticationException (AuthenticationException exception) {

        createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "The user is not authenticated or the credentials are not valid",
            exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @ExceptionHandler (AccessDeniedException.class)
    protected ResponseEntity<ProblemDetail> handleAccessDeniedException (AccessDeniedException exception) {
        createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "The authenticated user doesn't have enough permissions",
            exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler (InvalidBearerTokenException.class)
    protected ResponseEntity<ProblemDetail> handleInvalidBearerTokenException (InvalidBearerTokenException exception) {
        createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "The Bearer token is not valid",
            exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler (InsufficientAuthenticationException.class)
    protected ResponseEntity<ProblemDetail> handleInsufficientAuthenticationException (InsufficientAuthenticationException exception) {
        createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "Authentication is insufficient to access the resource",
            exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
