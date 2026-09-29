package com.ferave.userservice.dto;

import java.util.Set;

public record UserResponseDto (
    
    Long id, 

    String keyckoakId,
    
    String userName, 
    
    String email,

    Boolean enabled,

    Set<String> roles
) {
}