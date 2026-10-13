package com.ferave.userservice.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration 
@OpenAPIDefinition (
    info = @Info (
        title = "User API",
        version = "1.0.0",
        description = "User API documentation"
    )
)
public class OpenApiConfig {}
