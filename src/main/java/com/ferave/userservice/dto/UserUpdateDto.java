package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    @NotBlank 
    @Size (max = 50)
    String userName, 
    
    @Email
    @NotBlank 
    @Size (max = 250)
    String email, 
     
    Boolean enabled
) {
}
