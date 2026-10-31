package com.ferave.userservice.dto;

import java.util.UUID;

public record UserResponseDto (
    
    UUID id,
    
    String userName, 
    
    String email,

    Boolean enabled
) {
}