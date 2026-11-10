package com.ferave.userservice.integration.model;

public record UserIdentityResponse (

    String externalId,

    String username,

    String email,

    boolean enabled
) {

}
