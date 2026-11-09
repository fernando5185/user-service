package com.ferave.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(

    @Pattern (
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$",
        message = "The name must have a valid format"
    )
    @Size (max = 50)
    String userName, 
    
    @Email 
    @Pattern ( 
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",  
        message = "The email format isn't valid"  
    ) 
    @Size (max = 250)
    String email, 
     
    Boolean enabled
) {
}
