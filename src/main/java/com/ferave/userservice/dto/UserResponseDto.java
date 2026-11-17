package com.ferave.userservice.dto;

import java.util.UUID;

public record UserResponseDto (
    
    UUID id,
    
    String username, 
    
    String email,

    Boolean enabled
) {
}