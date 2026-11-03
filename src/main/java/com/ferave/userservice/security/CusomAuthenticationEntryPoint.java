package com.ferave.userservice.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class CusomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
            ) throws IOException, ServletException {


        ProblemDetail problem =ProblemDetail.forStatusAndDetail(
             HttpStatus.UNAUTHORIZED,
             "Invalid or missing token"
        );

        problem.setTitle("Unauthirized");
        problem.setProperty("timestamp", Instant.now());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        mapper.writeValue(response.getWriter(), problem);
    }

}
