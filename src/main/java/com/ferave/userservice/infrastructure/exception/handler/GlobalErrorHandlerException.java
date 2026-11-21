package com.ferave.userservice.infrastructure.exception.handler;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
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

import com.ferave.userservice.domain.exceptions.EmailAlreadyExistsException;
import com.ferave.userservice.domain.exceptions.ResourceNotFoundException;
import com.ferave.userservice.domain.exceptions.UserCreateException;
import com.ferave.userservice.domain.exceptions.UserNameAlreadyExistsException;
import com.ferave.userservice.infrastructure.exception.integration.KeycloakIntegrationException;
import com.ferave.userservice.infrastructure.exception.integration.KeycloakUserAlreadyExistsException;

@RestControllerAdvice 
public class GlobalErrorHandlerException extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> createProblemDetail(
        HttpStatusCode status,
        String title,
        String detail,
        String errorCode
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setType(
            URI.create("urn:error:" + errorCode.toLowerCase())
        );
        problem.setProperty("errorCode", errorCode);
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<Object> handleUnexpectedException (Exception exception) {
        return createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected Error",
            exception.getMessage(),
            "INTERNAL_SERVER_ERROR"
            
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.computeIfAbsent(error.getField(), key -> new ArrayList<>())
                    .add(error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(createProblemDetail(
            status,
            "Validation Failed",
            ex.getMessage(),
            "VALIDATION_FAILED")
        );
        
    }

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException exception) {
        return createProblemDetail(
            HttpStatus.NOT_FOUND, 
            "Resource not Found", 
            exception.getMessage(),
            "RESOURCE_NOT_FOUND");
    }

    @ExceptionHandler (EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyEmailExistsException(EmailAlreadyExistsException exception) {
        return createProblemDetail(
            HttpStatus.CONFLICT, 
            "The email address already exists",
            exception.getMessage(), 
            "EMAIL_ALREADY_EXISTS"
    );
    }

    @ExceptionHandler (KeycloakIntegrationException .class)
    public ResponseEntity<Object> handleKeycloakUserAlreadyExistsException(KeycloakIntegrationException exception) {
        return createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "An error occurred in Keycloak",
            exception.getMessage(),
            "KEYCLOAK_ERROR"
        
        );
    }

    public ResponseEntity<Object> handleKeycloakUserAlreadyExistsException(KeycloakUserAlreadyExistsException exception) {
        return createProblemDetail(
            HttpStatus.CONFLICT, 
            "Keycloak User already exists",
            exception.getMessage(),
            "KEYCLOAK_USER_ALREADY_EXISTS"
        
        );
    }

    @ExceptionHandler (UserCreateException.class)
    public ResponseEntity<Object> handleUserCreateException(UserCreateException exception) {
        return createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "User Creation Error",
            exception.getMessage(),
            "USER_CREATION_ERROR"
        );
    }

    @ExceptionHandler (UserNameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserNameAlreadyExistsException(UserNameAlreadyExistsException exception) {
        return createProblemDetail(
            HttpStatus.CONFLICT, 
            "Already resource exists",
            exception.getMessage(),
            "USERNAME_ALREADY_EXISTS"
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "Invalid JSON or unreadable request body",
            exception.getMessage(),
            "INVALID_JSON_OR_UNREADABLE_REQUEST_BODY"
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return createProblemDetail(
            HttpStatus.METHOD_NOT_ALLOWED, 
            "Method Not Allowed",
            exception.getMessage(),
            "METHOD_NOT_ALLOWED"
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "Missing Parameter",
            exception.getMessage(),
            "MISSING_PARAMETER"
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return createProblemDetail(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            "Unsupported content type",
            exception.getMessage(),
            "UNSUPPORTED_CONTENT_TYPE"
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Parameter is missing",
            exception.getMessage(),
            "MISSING_PATH_VARIABLE"
        );
    }

     @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        
        return createProblemDetail(
            HttpStatus.BAD_REQUEST, 
            "A parameter has the wrong type",
           exception.getMessage(),
            "INVALID_PARAMETER_TYPE"
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
                
        return createProblemDetail(
            HttpStatus.NOT_ACCEPTABLE, 
            "An acceptable response for the client can't be produced.",
            exception.getMessage(),
            "NOT_ACCEPTABLE"
        );
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException (
        DataIntegrityViolationException exception
    ) {
        return createProblemDetail(
            HttpStatus.CONFLICT,
            "The operation violates a data integrity constraint.",
            exception.getMessage(),
            "DATA_CONSTRAINT_VIOLATION"
        );
    }

    @ExceptionHandler (AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException (AuthenticationException exception) {

        return createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "Authentication Failed",
            exception.getMessage(),
            "AUTHENTICATION_FAILED"
        );
    }

    @ExceptionHandler (AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException (AccessDeniedException exception) {
        return createProblemDetail(
            HttpStatus.FORBIDDEN, 
            "Access Denied",
            exception.getMessage(),
            "ACCESS_DENIED"
        );
    }

    @ExceptionHandler (InvalidBearerTokenException.class)
    protected ResponseEntity<Object> handleInvalidBearerTokenException (InvalidBearerTokenException exception) {
        return createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "The Bearer token is not valid",
            exception.getMessage(),
            "INVALID_BEARER_TOKEN"
        );
    }

    @ExceptionHandler (InsufficientAuthenticationException.class)
    protected ResponseEntity<Object> handleInsufficientAuthenticationException (InsufficientAuthenticationException exception) {
        return createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "Authentication is insufficient to access the resource",
            exception.getMessage(),
            "INSUFFICIENT_AUTHENTICATION"
        );
    }
}
