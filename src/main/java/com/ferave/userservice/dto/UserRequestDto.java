package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto (
    @NotBlank 
    String userName, 
    
    @Email 
    String email
) {
}