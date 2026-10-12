package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDto(
    
    @NotBlank 
    String userName, 
    
    @Email String email, 
    
    @NotNull 
    Boolean enabled
) {
}
