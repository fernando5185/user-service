package com.ferave.userservice.exceptions;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

@RestControllerAdvice 
public class GlobalErrorHandlerException extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> createProblemDetail(
        HttpStatusCode status,
        String title,
        String detail
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problem);
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

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more validation errors occurred"
        );

        problemDetail.setTitle("Validation failed");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(problemDetail);
        
    }

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException exception) {
        return createProblemDetail(
            HttpStatus.NOT_FOUND, 
            "Resource not Found", 
            "The requested resource was not found. Check the information provided and try again.");
    }

    @ExceptionHandler (EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyEmailExistsException(EmailAlreadyExistsException exception) {
        return createProblemDetail(
            HttpStatus.CONFLICT, 
            "Already resource exists",
            "The email address provided is already registered in the system. Please use a different email address.");
    }

    @ExceptionHandler (UserNameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserNameAlreadyExistsException(UserNameAlreadyExistsException exception) {
        return createProblemDetail(
            HttpStatus.CONFLICT, 
            "Already resource exists",
            "The username provided is already registered in the system. Please use a different username.");
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
            "The request body is missing, malformed, or contains invalid values.");
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return createProblemDetail(
            HttpStatus.METHOD_NOT_ALLOWED, 
            "Method Not Supported",
            "The HTTP method is not supported for this endpoint.");
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
            "A required parameter is missing in the request. Check the required parameters and try again.");
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
            "The request Content-Type is not supported.");
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
            "A required path variable is missing from the server configuration.");
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
            "One or more request parameters have an invalid value or type.");
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
            "No response format compatible with the content types accepted in the request was found.");
    }

    @ExceptionHandler (AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException (AuthenticationException exception) {

        return createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "Authentication Failed",
        "Authentication is required or the supplied credentials are invalid.");
    }

    @ExceptionHandler (AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException (AccessDeniedException exception) {
        return createProblemDetail(
            HttpStatus.FORBIDDEN, 
            "Access Denied",
        "You do not have permission to access this resource.");
    }

    @ExceptionHandler (InvalidBearerTokenException.class)
    protected ResponseEntity<Object> handleInvalidBearerTokenException (InvalidBearerTokenException exception) {
        return createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "The Bearer token is not valid",
            "The supplied access token is invalid or expired.");
    }

    @ExceptionHandler (InsufficientAuthenticationException.class)
    protected ResponseEntity<Object> handleInsufficientAuthenticationException (InsufficientAuthenticationException exception) {
        return createProblemDetail(
            HttpStatus.UNAUTHORIZED, 
            "Authentication is insufficient to access the resource",
            "The request requires valid authentication to access the requested resource. Check your credentials and try again.");
    }
}
