package com.ferave.userservice.identity.dto;

public record UserIdentityResponseDto(

    String keycloakId,
    
    String username, 
    
    String email,

    Boolean enabled
) {

}
