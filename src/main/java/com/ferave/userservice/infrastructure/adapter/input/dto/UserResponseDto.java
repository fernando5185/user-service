package com.ferave.userservice.infrastructure.adapter.input.dto;

import java.util.UUID;

public record UserResponseDto (
    
    UUID id,

    String keycloakId,
    
    String username, 
    
    String email,

    Boolean enabled
) {
}