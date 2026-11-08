package com.ferave.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDto (
    @NotBlank
    @Pattern (
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$",
        message = "El nombre debe contener un formato valido"
    )
    @Size (max = 50)
    String userName, 
    
    @NotBlank(message = "El correo es obligatorio") 
    @Pattern ( 
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",  
        message = "El formato del correo electrónico no es válido" 
    )
    @Size (max = 250)
    String email
) {
}