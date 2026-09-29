package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDto(
    
    String userName, 
    
    @Email String email, 
    
    Boolean enabled
) {
}
