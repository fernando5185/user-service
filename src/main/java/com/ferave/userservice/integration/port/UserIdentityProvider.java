package com.ferave.userservice.integration.port;

import com.ferave.userservice.integration.model.UserIdentityRequest;
import com.ferave.userservice.integration.model.UserIdentityResponse;

public interface UserIdentityProvider {

    UserIdentityResponse saveUserIdentity(UserIdentityRequest userIdentityRequest);

    UserIdentityResponse getUserIdentityById(String keycloakId);

    void deleteUserIdentityById(String keycloakId);
}
