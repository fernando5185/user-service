package com.ferave.userservice.integration.port;

import java.util.List;

import com.ferave.userservice.integration.model.UserIdentityRequest;
import com.ferave.userservice.integration.model.UserIdentityResponse;

public interface UserIdentityProvider {

    UserIdentityResponse saveUserIdentity(UserIdentityRequest userIdentityRequest);

    List<UserIdentityResponse> usersIdentity();

    UserIdentityResponse getUserIdentityById(String keycloakId);

    void deleteUserIdentityById(String keycloakId);
}
