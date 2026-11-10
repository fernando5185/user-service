package com.ferave.userservice.integration.model;

public record UserIdentityResponse (

    String id,

    String username,

    String email,

    boolean enabled
) {

}
