package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
     
    @Size (max = 50)
    String userName, 
    
    @Email
    String email, 
     
    Boolean enabled
) {
}
