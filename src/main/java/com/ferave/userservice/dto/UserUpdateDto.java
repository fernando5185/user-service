package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UserUpdateDto(
    
    @NotBlank 
    String userName, 
    
    @Email String email, 
    
    @NotEmpty 
    Boolean enabled
) {
}
